package greeny.backend.domain.member.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class EditMemberInfoRequestDto {
    @NotBlank(message = "기존 비밀번호를 입력해주세요.")
    @Pattern(regexp = "^(?=.*[a-zA-Z0-9])(?=.*[!@#$%^&*]).{8,}$", message = "비밀번호는 영숫자, 특수문자가 필수이고 8자리 이상이어야 합니다.")
    @Schema(description = "기존 비밀번호", defaultValue = "test1234!")
    private String passwordToCheck;

    @NotBlank(message = "새로운 비밀번호를 입력해주세요.")
    @Pattern(regexp = "^(?=.*[a-zA-Z0-9])(?=.*[!@#$%^&*]).{8,15}$", message = "비밀번호는 영숫자, 특수문자가 필수이고 8자리 이상 15이내여야 합니다.")
    @Schema(description = "새로운 비밀번호", defaultValue = "test5678@")
    private String passwordToChange;
}