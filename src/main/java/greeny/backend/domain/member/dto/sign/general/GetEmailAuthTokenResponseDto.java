package greeny.backend.domain.member.dto.sign.general;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetEmailAuthTokenResponseDto {  // 이메일 전송 시 생성하는 token 반환
    private String token;
}
