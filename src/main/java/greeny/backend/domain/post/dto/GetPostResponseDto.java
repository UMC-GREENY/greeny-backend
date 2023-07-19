package greeny.backend.domain.post.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import greeny.backend.domain.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetPostResponseDto {
    private Long id;
//    private String writeEmail;
    private String createdAt;
    private String title;
    private String content;
    private List<String> fileUrls;

    public static GetPostResponseDto from(Post post){
        return GetPostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .createdAt(post.getCreatedAt())
                .content(post.getContent())
                .fileUrls(post.getFileUrls())
                .build();
    }

}
