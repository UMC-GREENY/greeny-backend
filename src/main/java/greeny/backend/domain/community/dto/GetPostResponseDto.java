package greeny.backend.domain.community.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import greeny.backend.domain.community.entity.Post;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetPostResponseDto {
    private Long id;
    private String writeEmail;
    private String createdAt;
    private String updatedAt;
    private String title;
    private String content;
    private List<String> fileUrls;

    public static GetPostResponseDto from(Post post){
        return GetPostResponseDto.builder()
                .id(post.getId())
                .writeEmail(post.getWriter().getEmail())
                .title(post.getTitle())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .content(post.getContent())
                .fileUrls(post.getFileUrls())
                .build();
    }

}
