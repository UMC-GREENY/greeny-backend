package greeny.backend.domain.member.presentation.dto.sign.general;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class AuthEmailRequestDto {
    @NotBlank(message = "이메일을 입력해주세요.")
    @Pattern(regexp = "^[A-Za-z0-9]+@(gmail\\.com|naver\\.com)$", message = "이메일은 google, naver 메일만 사용 가능합니다.")
    @Schema(description = "이메일")
    private String email;

    @NotBlank(message = "인가 url 을 입력해주세요.")
    @Schema(description = "인가 url")
    private String authorizationUrl;
}