package greeny.backend.domain.member.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class CancelBookmarkRequestDto {
    @Schema(description = "삭제할 스토어 or 제품 북마크 id 리스트")
    private List<Long> idsToDelete;
}