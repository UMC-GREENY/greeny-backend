package greeny.backend.domain.review.presentation.dto;

import lombok.*;
import java.util.List;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class GetReviewInfoResponseDto {
    private String writerEmail;
    private String createdAt;
    private Integer star;
    private String content;
    private List<String> fileUrls;
    private boolean isWriter;
}