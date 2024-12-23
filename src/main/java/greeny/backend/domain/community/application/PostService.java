package greeny.backend.domain.community.application;

import greeny.backend.infra.aws.S3Service;
import greeny.backend.domain.community.entity.PostLike;
import greeny.backend.domain.member.entity.Member;
import greeny.backend.domain.community.presentation.dto.WritePostRequestDto;
import greeny.backend.domain.community.entity.Post;
import greeny.backend.domain.community.entity.PostFile;
import greeny.backend.domain.community.entity.PostRepository;
import greeny.backend.domain.community.presentation.dto.GetSimplePostInfosResponseDto;
import greeny.backend.domain.community.presentation.dto.GetPostInfoResponseDto;
import greeny.backend.exception.situation.MemberNotEqualsException;
import greeny.backend.exception.situation.PostNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final S3Service s3Service;

    @Transactional
    public void writePost(WritePostRequestDto writePostRequestDto, List<MultipartFile> postFiles, Member writer) {
        if(checkEmptyPostFiles(postFiles)){
            save(writePostRequestDto.toEntity(writer, false));
            return;
        }
        uploadPostFiles(postFiles, save(writePostRequestDto.toEntity(writer, true)));
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
        Post post = postRepository.findByIdWithWriterAndPostFilesAndPostLikes(postId).orElseThrow(PostNotFoundException::new);
        post.updateHits();
        return GetPostInfoResponseDto.from(post, false, false);
    }

    @Transactional
    public GetPostInfoResponseDto getPostInfoWithAuthMember(Long postId, Member currentMember) {
        Post post = postRepository.findByIdWithWriterAndPostFilesAndPostLikes(postId).orElseThrow(PostNotFoundException::new);
        post.updateHits();
        return GetPostInfoResponseDto.from(post,
                isWriter(post, currentMember),
                isLiked(post, currentMember));
    }

    @Transactional(readOnly = true)
    public Boolean isWriter(Post post, Member currentMember){
        return post.getWriter().getId().equals(currentMember.getId());
    }

    @Transactional(readOnly = true)
    public Boolean isLiked(Post post, Member currentMember){
        for(PostLike postLike : post.getPostLikes()){
            if(postLike.getLiker().getId().equals(currentMember.getId())) return true;
        }
        return false;
    }

    @Transactional
    public void deletePost(Long postId, Member currentMember) {
        Post post = postRepository.findByIdWithWriterAndPostFiles(postId).orElseThrow(PostNotFoundException::new);

        if(!post.getWriter().getId().equals(currentMember.getId())) throw new MemberNotEqualsException();

        List<String> fileUrls = new ArrayList<>();
        for(String fileUrl : post.getFileUrls()) fileUrls.add(fileUrl);
        postRepository.delete(post);
        for(String fileUrl : fileUrls) s3Service.deleteFile(fileUrl);
    }

    @Transactional
    public void editPostInfo(Long postId, WritePostRequestDto editPostInfoRequestDto, List<MultipartFile> postFiles, Member currentMember) {
        Post post = postRepository.findByIdWithWriterAndPostFiles(postId).orElseThrow(PostNotFoundException::new);
        if(!post.getWriter().getId().equals(currentMember.getId()))
            throw new MemberNotEqualsException();
        List<String> fileUrls = post.getFileUrls();
        post.getPostFiles().clear();

        if (checkEmptyPostFiles(postFiles))
            update(post, editPostInfoRequestDto.getTitle(), editPostInfoRequestDto.getContent(), false);
        if (!checkEmptyPostFiles(postFiles)) {
            update(post, editPostInfoRequestDto.getTitle(), editPostInfoRequestDto.getContent(), true);
            uploadPostFiles(postFiles, post);
        }

        for(String fileUrl : fileUrls)
            s3Service.deleteFile(fileUrl);
    }

    public void uploadPostFiles(List<MultipartFile> postFiles, Post post) {
        for(MultipartFile multipartFile : postFiles){
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

    private boolean checkEmptyPostFiles(List<MultipartFile> postFiles) {
        return postFiles.isEmpty();
    }

    private Post save(Post post) {
        return postRepository.save(post);
    }

    public void update(Post post, String updatedTitle, String updatedContent, Boolean hasPostFile) {
        post.update(updatedTitle, updatedContent, hasPostFile);
    }
}