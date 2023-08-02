package greeny.backend.domain.board.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import greeny.backend.domain.board.entity.Post;
import lombok.*;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetSimplePostInfosResponseDto {

    private Long id;
    private String createdAt;
    private String writerEmail;
    private String title;
    private Boolean existsFile;

    public static GetSimplePostInfosResponseDto from(Post post){
        return GetSimplePostInfosResponseDto.builder()
                .id(post.getId())
                .writerEmail(post.getWriter().getEmail())
                .createdAt(post.getCreatedAt())
                .title(post.getTitle())
                .existsFile(post.checkFileExist())
                .build();
    }

}
