package greeny.backend.domain.post.service;

import greeny.backend.config.aws.S3Service;
import greeny.backend.domain.post.dto.CreatePostRequestDto;
import greeny.backend.domain.post.dto.GetPostListResponseDto;
import greeny.backend.domain.post.dto.GetPostResponseDto;
import greeny.backend.domain.post.dto.UpdatePostRequestDto;
import greeny.backend.domain.post.entity.Post;
import greeny.backend.domain.post.entity.PostFile;
import greeny.backend.domain.post.repository.PostFileRepository;
import greeny.backend.domain.post.repository.PostRepository;
import greeny.backend.exception.situation.PostNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostFileRepository postFileRepository;
    private final S3Service s3Service;

    @Transactional
    public void creatPost(CreatePostRequestDto createPostRequestDto, List<MultipartFile> multipartFiles) {
        Post post = postRepository.save(createPostRequestDto.toEntity());
        //파일을 첨부한 경우
        if(multipartFiles != null){
            // s3에 첨부파일을 저장하고, DB에도 첨부파일주소를 저장
            for(MultipartFile multipartFile : multipartFiles){
                PostFile postFile = PostFile.builder()
                        .fileUrl(s3Service.uploadFile(multipartFile))
                        .post(post).build();
                post.getPostFiles().add(postFile);
            }
        }
    }

    @Transactional(readOnly = true)
    public Page<GetPostListResponseDto> getPosts(Pageable pageable) {
        return postRepository.findAll(pageable)
                .map(GetPostListResponseDto::from);
    }

    @Transactional(readOnly = true)
    public Page<GetPostListResponseDto> searchPosts(String keyword, Pageable pageable) {
        if(keyword==null || keyword.trim().isEmpty()) return getPosts(pageable);
        return postRepository.findByTitleContaining(keyword, pageable)
                .map(GetPostListResponseDto::from);
    }

    @Transactional
    public GetPostResponseDto getPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        post.updateHits();
        return GetPostResponseDto.from(post);
    }

    @Transactional
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        List<String> fileUrls = new ArrayList<>();
        for(String fileUrl : post.getFileUrls()) fileUrls.add(fileUrl);
        postRepository.delete(post);
        for(String fileUrl : fileUrls) s3Service.deleteFile(fileUrl);
    }

    @Transactional
    public void updatePost(Long postId, UpdatePostRequestDto updatePostRequestDto, List<MultipartFile> multipartFiles) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        //게시글의 제목과 내용 업데이트
        post.update(updatePostRequestDto.getTitle(), updatePostRequestDto.getContent());

        List<String> fileUrls = post.getFileUrls();
        post.getPostFiles().clear(); // 1. DB에서 게시글의 기존 첨부파일주소를 모두 삭제
        //파일을 첨부한 경우
        if(multipartFiles != null){
            // 2. s3에 첨부파일을 저장하고, DB에도 첨부파일주소를 저장
            for(MultipartFile multipartFile : multipartFiles){
                PostFile postFile = PostFile.builder()
                        .fileUrl(s3Service.uploadFile(multipartFile))
                        .post(post).build();
                post.getPostFiles().add(postFile);
            }
        }
        // 3. 1번에서 DB에서 삭제했던 첨부파일주소에 해당하는 s3의 첨부파일을 모두 삭제
        for(String fileUrl : fileUrls) s3Service.deleteFile(fileUrl);
    }

//    private static void authorizeWriter(Post post){
//        String email = SecurityContextHolder.getContext().getAuthentication().getName();
//        if(!post.getWriter.getEmail().equals(email)){
//            throw new MemberNotEqualsException();
//        }
//    }
}
