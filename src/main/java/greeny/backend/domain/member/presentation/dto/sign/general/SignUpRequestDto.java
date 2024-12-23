package greeny.backend.domain.member.presentation.dto.sign.general;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class SignUpRequestDto {
    @NotBlank(message = "이메일을 입력해주세요.")
    @Pattern(regexp = "^[A-Za-z0-9]+@(gmail\\.com|naver\\.com)$", message = "이메일은 google, naver 메일만 사용 가능합니다.")
    @Schema(description = "이메일", defaultValue = "test@gmail.com")
    private String email;

    @Pattern(regexp = "^(?=.*[a-zA-Z0-9])(?=.*[!@#$%^&*]).{8,15}$", message = "비밀번호는 영숫자, 특수문자가 필수이고 8자리 이상 15이내여야 합니다.")
    @Schema(description = "비밀번호", defaultValue = "test1234!")
    private String password;

    @NotBlank(message = "이름을 입력해주세요.")
    @Size(min = 2, message = "이름이 너무 짧습니다.")
    @Schema(description = "이름", defaultValue = "홍길동")
    private String name;

    @NotBlank(message = "휴대폰 번호를 입력해주세요.")
    @Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$", message = "휴대폰 번호가 잘못 입력되었습니다.")
    @Schema(description = "휴대폰 번호", defaultValue = "010-1234-5678")
    private String phone;

    @NotBlank(message = "생년월일을 입력해주세요.")
    @Pattern(regexp = "^(19[0-9][0-9]|20\\d{2})-(0[0-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$", message = "생년월일이 잘못 입력되었습니다.")
    @Schema(description = "생년월일", defaultValue = "2000-01-01")
    private String birth;
}