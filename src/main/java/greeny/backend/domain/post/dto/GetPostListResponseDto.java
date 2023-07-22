package greeny.backend.domain.post.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import greeny.backend.domain.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetPostListResponseDto {

    private Long id;
    private String createdAt;
    private String writerEmail;
    private String title;
    private Boolean existsFile;

    public static GetPostListResponseDto from(Post post){
        return GetPostListResponseDto.builder()
                .id(post.getId())
                .createdAt(post.getCreatedAt())
                .title(post.getTitle())
                .existsFile(post.checkFileExist())
                .build();
    }

}
