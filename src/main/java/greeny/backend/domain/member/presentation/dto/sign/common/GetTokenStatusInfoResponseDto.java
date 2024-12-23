package greeny.backend.domain.member.presentation.dto.sign.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetTokenStatusInfoResponseDto {
    private Boolean isValid;
}