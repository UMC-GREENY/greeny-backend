package greeny.backend.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetReviewInfoResponseDto {
    private String writerEmail;
    private String createdAt;
    private Integer star;
    private String content;
    private List<String> fileUrls;
    private boolean isWriter;

}
