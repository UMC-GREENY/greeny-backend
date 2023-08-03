package greeny.backend.domain.member.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditMemberInfoRequestDto {
    //현재 회원의 비밀번호가 맞는지 확인합니다.
    @NotBlank(message = "기존 비밀번호를 입력해주세요.")
    @Pattern(regexp = "^(?=.*[a-zA-Z0-9])(?=.*[!@#$%^&*]).{8,}$", message = "비밀번호는 영숫자, 특수문자가 필수이고 8자리 이상이어야 합니다.")
    @Schema(description = "기존 비밀번호", defaultValue = "test1234!")
    private String passwordToCheck;

    //회원이 바꾸려는 비밀번호를 입력받습니다.
    @NotBlank(message = "새로운 비밀번호를 입력해주세요.")
    @Pattern(regexp = "^(?=.*[a-zA-Z0-9])(?=.*[!@#$%^&*]).{8,15}$", message = "비밀번호는 영숫자, 특수문자가 필수이고 8자리 이상 15이내여야 합니다.")
    @Schema(description = "새로운 비밀번호", defaultValue = "test5678@")
    private String passwordToChange;
}
