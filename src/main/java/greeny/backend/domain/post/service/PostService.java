package greeny.backend.domain.post.service;

import greeny.backend.config.aws.S3Service;
import greeny.backend.domain.post.dto.CreatePostRequestDto;
import greeny.backend.domain.post.dto.GetPostListResponseDto;
import greeny.backend.domain.post.dto.GetPostResponseDto;
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
        if(multipartFiles!=null && !multipartFiles.isEmpty()){
            for(MultipartFile multipartFile : multipartFiles){
                PostFile postFile = PostFile.builder()
                        .fileUrl(s3Service.uploadFile(multipartFile))
                        .post(post).build();
                postFileRepository.save(postFile);
            }
        }
    }

    @Transactional(readOnly = true)
    public Page<GetPostListResponseDto> searchPosts(String keyword, Pageable pageable) {
        if(keyword==null || keyword.trim().isEmpty()) {
            return postRepository.findAll(pageable)
                    .map(GetPostListResponseDto::from);
        }
        return postRepository.findByTitleContaining(keyword, pageable)
                .map(GetPostListResponseDto::from);
    }

    @Transactional(readOnly = true)
    public GetPostResponseDto getPost(Long postId) {
        return GetPostResponseDto.from(postRepository.findById(postId).orElseThrow(PostNotFoundException::new));
    }

    @Transactional
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        List<String> fileUrls = new ArrayList<>();
        for(String fileUrl : post.getFileUrls()) fileUrls.add(fileUrl);
        postRepository.delete(post);
        for(String fileUrl : fileUrls) s3Service.deleteFile(fileUrl);
    }

//    private static void authorizeWriter(Post post){
//        String email = SecurityContextHolder.getContext().getAuthentication().getName();
//        if(!post.getWriter.getEmail().equals(email)){
//            throw new MemberNotEqualsException();
//        }
//    }
}
