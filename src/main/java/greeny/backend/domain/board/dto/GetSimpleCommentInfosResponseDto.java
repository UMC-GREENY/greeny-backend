package greeny.backend.domain.board.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import greeny.backend.domain.board.entity.Comment;
import lombok.*;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetSimpleCommentInfosResponseDto {

    private Long id;
    private String createdAt;
    private String updatedAt;
    private String writerEmail;
    private String content;
    private Boolean isWriter; // 화면에 수정,삭제 버튼 띄울지 판단할 때 필요

    public static GetSimpleCommentInfosResponseDto from(Comment comment, Boolean isWriter) {
        return GetSimpleCommentInfosResponseDto.builder()
                .id(comment.getId())
                .writerEmail(comment.getWriter().getEmail())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .content(comment.getContent())
                .isWriter(isWriter)
                .build();
    }
}
