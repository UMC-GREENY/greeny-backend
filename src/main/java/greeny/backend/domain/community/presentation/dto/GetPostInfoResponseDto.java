package greeny.backend.domain.community.presentation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import greeny.backend.domain.community.entity.Post;
import lombok.*;
import java.util.List;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetPostInfoResponseDto {
    private Long id;
    private String writerEmail;
    private String createdAt;
    private String updatedAt;
    private String title;
    private String content;
    private List<String> fileUrls;
    private Boolean isWriter;
    private Integer likes;
    private Boolean isLiked;

    public static GetPostInfoResponseDto from(Post post, Boolean isWriter, Boolean liked){
        return GetPostInfoResponseDto.builder()
                .id(post.getId())
                .writerEmail(post.getWriter().getEmail())
                .title(post.getTitle())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .content(post.getContent())
                .fileUrls(post.getFileUrls())
                .isWriter(isWriter)
                .likes(post.getPostLikes().size())
                .isLiked(liked)
                .build();
    }
}