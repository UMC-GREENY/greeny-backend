package greeny.backend.domain.board.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import greeny.backend.domain.board.entity.Post;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetPostInfoResponseDto {
    private Long id;
    private String writerEmail;
    private String createdAt;
    private String updatedAt;
    private String title;
    private String content;
    private List<String> fileUrls;
    private Boolean isWriter; // 화면에 수정,삭제 버튼 띄울지 판단할 때 필요

    public static GetPostInfoResponseDto from(Post post, Boolean isWriter){
        return GetPostInfoResponseDto.builder()
                .id(post.getId())
                .writerEmail(post.getWriter().getEmail())
                .title(post.getTitle())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .content(post.getContent())
                .fileUrls(post.getFileUrls())
                .isWriter(isWriter)
                .build();
    }

}
