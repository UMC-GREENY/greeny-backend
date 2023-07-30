package greeny.backend.domain.member.dto.sign.general;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthEmailRequestDto {  // 이메일, 인가 url 요청 받기
    @NotBlank(message = "이메일을 입력해주세요.")
    @Pattern(regexp = "^[A-Za-z0-9]+@(gmail\\.com|naver\\.com)$", message = "이메일은 google, naver 메일만 사용 가능합니다.")  // 영숫자 1자 이상인 gmail or naver
    @Schema(description = "이메일")
    private String email;
    @NotBlank(message = "인가 url 을 입력해주세요.")
    @Schema(description = "인가 url")
    private String authorizationUrl;
}
