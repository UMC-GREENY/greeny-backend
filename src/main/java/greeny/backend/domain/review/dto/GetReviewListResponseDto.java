package greeny.backend.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetReviewListResponseDto {
    private Long id;
    private String createdAt;
    private String writerEmail;
    private int star;
    private String content;
    private boolean existsFile;

}
