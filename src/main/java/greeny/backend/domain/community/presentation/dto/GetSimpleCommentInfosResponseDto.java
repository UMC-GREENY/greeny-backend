package greeny.backend.domain.community.presentation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import greeny.backend.domain.community.entity.Comment;
import lombok.*;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetSimpleCommentInfosResponseDto {

    private Long id;
    private String createdAt;
    private String updatedAt;
    private String writerEmail;
    private String content;
    private Boolean isWriter;

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