package greeny.backend.domain.board.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import greeny.backend.domain.board.entity.Comment;
import greeny.backend.domain.board.entity.Post;
import lombok.*;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetCommentListResponseDto {

    private Long id;
    private String createdAt;
    private String updatedAt;
    private String writerEmail;
    private String content;

    public static GetCommentListResponseDto from(Comment comment) {
        return GetCommentListResponseDto.builder()
                .id(comment.getId())
                .writerEmail(comment.getWriter().getEmail())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .content(comment.getContent())
                .build();
    }
}