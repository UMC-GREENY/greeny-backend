package greeny.backend.domain.post.service;

import greeny.backend.config.aws.S3Service;
import greeny.backend.domain.post.dto.CreatePostRequestDto;
import greeny.backend.domain.post.entity.Post;
import greeny.backend.domain.post.entity.PostFile;
import greeny.backend.domain.post.repository.PostFileRepository;
import greeny.backend.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
}
