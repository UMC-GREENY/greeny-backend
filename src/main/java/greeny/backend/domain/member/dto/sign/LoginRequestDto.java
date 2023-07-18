package greeny.backend.domain.member.dto.sign;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {

    @NotBlank(message = "이메일을 입력해주세요.")
    @Pattern(regexp = "^[A-Za-z0-9]+@(gmail\\.com|naver\\.com)$", message = "이메일은 google, naver 메일만 사용 가능합니다.")  // 영숫자 1자 이상인 gmail or naver
    @Schema(description = "이메일", defaultValue = "test@gmail.com")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "^(?=.*[a-zA-Z0-9])(?=.*[!@#$%^&*]).{8,}$", message = "비밀번호는 영숫자, 특수문자가 필수이고 8자리 이상이어야 합니다.")  // 영숫자, 특수문자 (!@#$%^&*) 필수 => 8자리 이상
    @Schema(description = "비밀번호", defaultValue = "test1234!")
    private String password;

    @NotBlank(message = "자동 로그인 여부를 입력해주세요.")
    @Schema(description = "자동 로그인 여부", defaultValue = "0")
    private String isAuto;
}
