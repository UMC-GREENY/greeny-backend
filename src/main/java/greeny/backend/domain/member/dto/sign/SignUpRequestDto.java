package greeny.backend.domain.member.dto.sign;

import greeny.backend.domain.member.entity.Member;
import greeny.backend.domain.member.entity.MemberGeneral;
import greeny.backend.domain.member.entity.MemberProfile;
import greeny.backend.domain.member.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequestDto {
    @NotBlank(message = "이메일을 입력해주세요.")
    @Pattern(regexp = "^[A-Za-z0-9]+@(gmail\\.com|naver\\.com)$")  // 영숫자 1자 이상인 gmail or naver
    @Schema(description = "이메일", defaultValue = "test@gmail.com")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!@#$%^&*]{8,}$|^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")  // 영숫자 각각 1자 이상 + 특수 문자 (!@#$%^&*) 선택 => 8자리 이상
    @Schema(description = "비밀번호", defaultValue = "test1234!")
    private String password;

    @NotBlank(message = "이름을 입력해주세요.")
    @Size(min = 2, message = "이름이 너무 짧습니다.")
    @Schema(description = "이름", defaultValue = "홍길동")
    private String name;

    @NotBlank(message = "휴대폰 번호를 입력해주세요.")
    @Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$", message = "휴대폰 번호는 하이픈(-)을 포함해야 합니다.")
    @Schema(description = "휴대폰 번호", defaultValue = "010-1234-5678")
    private String phone;

    @NotBlank(message = "생년월일을 입력해주세요.")
    @Pattern(regexp = "^(19[0-9][0-9]|20\\d{2})-(0[0-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$", message = "생년월일은 하이픈(-)을 포함해야 합니다.")
    @Schema(description = "생년월일", defaultValue = "2000-01-01")
    private String birth;
}
