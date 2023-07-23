package greeny.backend.domain.member.dto.sign.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetTokenStatusInfoResponseDto {  // 토큰 유효성에 대한 정보를 제공하는 dto
    private String isValid;  // 토큰이 유효한지 아닌지
}
