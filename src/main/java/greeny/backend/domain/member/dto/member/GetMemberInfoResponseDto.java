package greeny.backend.domain.member.dto.member;

import com.fasterxml.jackson.annotation.JsonInclude;
import greeny.backend.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetMemberInfoResponseDto {

    private String loginType;
    private String email;
    private String name;
    private String phone;
    private String birth;

    public static GetMemberInfoResponseDto toGeneralMemberDto(String email, String name, String phone, String birth) {
        return GetMemberInfoResponseDto.builder()
                .loginType("General")
                .email(email)
                .name(name)
                .phone(phone)
                .birth(birth)
                .build();
    }

    public static GetMemberInfoResponseDto toSocialMemberDto(String loginType) {
        return GetMemberInfoResponseDto.builder()
                .loginType(loginType)
                .build();
    }

}
