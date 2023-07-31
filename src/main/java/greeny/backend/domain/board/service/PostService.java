package greeny.backend.domain.board.service;

import greeny.backend.config.aws.S3Service;
import greeny.backend.domain.board.repository.PostLikeRepository;
import greeny.backend.domain.member.entity.Member;
import greeny.backend.domain.board.dto.WritePostRequestDto;
import greeny.backend.domain.board.dto.GetSimplePostInfosResponseDto;
import greeny.backend.domain.board.dto.GetPostInfoResponseDto;
import greeny.backend.domain.board.entity.Post;
import greeny.backend.domain.board.entity.PostFile;
import greeny.backend.domain.board.repository.PostRepository;
import greeny.backend.exception.situation.MemberNotEqualsException;
import greeny.backend.exception.situation.PostNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final S3Service s3Service;

    @Transactional
    public void writePost(WritePostRequestDto writePostRequestDto, List<MultipartFile> multipartFiles, Member writer) {
        // s3에 파일을 업로드 한 뒤 예외가 발생하면 db는 롤백이 되지만,
        // 이미 s3에 저장된 이미지는 삭제되지 않는 문제가 있음.

        // post를 먼저 저장
        Post post = postRepository.save(writePostRequestDto.toEntity(writer));
        //파일을 첨부한 경우
        if(multipartFiles != null){
            // s3에 첨부파일을 저장하고, db에도 post_file을 저장
            uploadPostFileList(multipartFiles, post);
        }
    }

    @Transactional(readOnly = true)
    public Page<GetSimplePostInfosResponseDto> getSimplePostInfos(Pageable pageable) {
        return postRepository.findAll(pageable)
                .map(GetSimplePostInfosResponseDto::from);
    }

    @Transactional(readOnly = true)
    public Page<GetSimplePostInfosResponseDto> searchSimplePostInfos(String keyword, Pageable pageable) {
        if(!StringUtils.hasText(keyword)) return getSimplePostInfos(pageable);
        return postRepository.findAllByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword, keyword, pageable)
                .map(GetSimplePostInfosResponseDto::from);
    }

    @Transactional
    public GetPostInfoResponseDto getPostInfo(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        post.updateHits();
        return GetPostInfoResponseDto.from(post, false, false);
    }

    // 인증된 사용자의 게시글 상세정보 조회
    @Transactional
    public GetPostInfoResponseDto getPostInfoWithAuthMember(Long postId, Member currentMember) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        post.updateHits();
        return GetPostInfoResponseDto.from(post,
                isWriter(post, currentMember),
                isLiked(post, currentMember));
    }

    @Transactional(readOnly = true)
    public Boolean isWriter(Post post, Member currentMember){ // 게시글을 조회하는 사용자가 작성자인지 확인
        return post.getWriter().getId().equals(currentMember.getId());
    }

    @Transactional(readOnly = true)
    public Boolean isLiked(Post post, Member currentMember){ // 게시글을 조회하는 사용자가 좋아요를 눌렀는지 확인
        return postLikeRepository.existsByPostIdAndLikerId(post.getId(), currentMember.getId());
    }

    @Transactional
    public void deletePost(Long postId, Member currentMember) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);

        if(!post.getWriter().getId().equals(currentMember.getId())) throw new MemberNotEqualsException(); // 글쓴이 본인인지 확인

        List<String> fileUrls = new ArrayList<>();
        for(String fileUrl : post.getFileUrls()) fileUrls.add(fileUrl);
        postRepository.delete(post); //
        for(String fileUrl : fileUrls) s3Service.deleteFile(fileUrl);
    }

    @Transactional
    public void editPostInfo(Long postId, WritePostRequestDto editPostInfoRequestDto, List<MultipartFile> multipartFiles, Member currentMember) {
        // s3에 파일을 업로드 한 뒤 예외가 발생하면 db는 롤백이 되지만,
        // 이미 s3에 저장된 이미지는 삭제되지 않는 문제가 있음.

        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);

        if(!post.getWriter().getId().equals(currentMember.getId())) throw new MemberNotEqualsException(); // 글쓴이 본인인지 확인

        //게시글의 제목과 내용 업데이트
        post.update(editPostInfoRequestDto.getTitle(), editPostInfoRequestDto.getContent());

        List<String> fileUrls = post.getFileUrls();

        post.getPostFiles().clear(); // 1. db에서 게시글의 기존 post_file을 모두 삭제
        //파일을 첨부한 경우
        if(multipartFiles != null){
            // 2. s3에 파일을 저장하고, db에도 post_file을 저장
            uploadPostFileList(multipartFiles, post);
        }
        // 3. 1번에서 db에서 삭제했던 post_file에 해당하는 s3의 파일을 모두 삭제
        //    (s3는 트랜잭션 롤백이 안되기 때문에 삭제는 무조건 마지막에 해야함)
        for(String fileUrl : fileUrls) s3Service.deleteFile(fileUrl);
    }

    @Transactional
    protected void uploadPostFileList(List<MultipartFile> multipartFiles, Post post) {
        // s3에 파일을 업로드 한 뒤 예외가 발생하면 db는 롤백이 되지만,
        // 이미 s3에 저장된 이미지는 삭제되지 않는 문제가 있음.

        // s3에 첨부파일을 저장하고, mysql에도 post_file을 저장
        for(MultipartFile multipartFile : multipartFiles){
            PostFile postFile = PostFile.builder()
                    .fileUrl(s3Service.uploadFile(multipartFile))
                    .post(post).build();
            post.getPostFiles().add(postFile);
        }
    }

    @Transactional
    public Page<GetSimplePostInfosResponseDto> getMySimplePostInfos(Pageable pageable, Member currentMember) {
        return postRepository.findAllByWriterId(currentMember.getId(), pageable)
                .map(GetSimplePostInfosResponseDto::from);
    }
}
