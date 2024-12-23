package greeny.backend.domain.member.presentation.dto.sign.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class AgreementRequestDto {
    @NotBlank(message = "이메일을 입력해주세요.")
    @Pattern(regexp = "^[A-Za-z0-9]+@(gmail\\.com|naver\\.com)$", message = "이메일은 google, naver 메일만 사용 가능합니다.")
    @Schema(description = "이메일", defaultValue = "test@gmail.com")
    private String email;

    @NotNull(message = "개인 정보 수집 및 이용 동의 여부를 입력해주세요.")
    @Schema(description = "개인 정보 수집 및 이용 동의 (선택)", defaultValue = "true")
    private Boolean personalInfo;

    @NotNull(message = "개인 정보 제 3자 제공 동의 여부를 입력해주세요.")
    @Schema(description = "개인 정보 제 3자 제공 (선택)", defaultValue = "false")
    private Boolean thirdParty;
}