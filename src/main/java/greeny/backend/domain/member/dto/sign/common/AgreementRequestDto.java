package greeny.backend.domain.member.dto.sign.common;

import greeny.backend.domain.member.entity.Member;
import greeny.backend.domain.member.entity.MemberAgreement;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgreementRequestDto {  // 사용자에 대한 정보 제공 동의 여부를 받는 dto

    @NotBlank(message = "이메일을 입력해주세요.")
    @Pattern(regexp = "^[A-Za-z0-9]+@(gmail\\.com|naver\\.com)$", message = "이메일은 google, naver 메일만 사용 가능합니다.")  // 영숫자 1자 이상인 gmail or naver
    @Schema(description = "이메일", defaultValue = "test@gmail.com")
    private String email;  // 일반 회원가입 or 최초 소셜 로그인 이메일 계정 -> DB 에서 이메일을 통해 Member 객체를 찾고 해당 Member 의 동의 여부를 DB에 저장할 수 있음

    @NotBlank(message = "개인 정보 수집 및 이용 동의 여부를 입력해주세요.")
    @Schema(description = "개인 정보 수집 및 이용 동의 (선택)", defaultValue = "1")
    private String personalInfo;

    @NotBlank(message = "개인 정보 제 3자 제공 동의 여부를 입력해주세요.")
    @Schema(description = "개인 정보 제 3자 제공 (선택)", defaultValue = "0")
    private String thirdParty;

    public MemberAgreement toMemberAgreement(Member member, String personalInfo, String thirdParty) {  // MemberAgreement 객체로 변환
        return MemberAgreement.builder()
                .member(member)
                .personalInfo(personalInfo)
                .thirdParty(thirdParty)
                .build();
    }
}
