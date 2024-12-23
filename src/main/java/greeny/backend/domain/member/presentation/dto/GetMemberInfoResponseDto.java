package greeny.backend.domain.member.presentation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
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

    public static GetMemberInfoResponseDto toSocialMemberDto(String email, String loginType) {
        return GetMemberInfoResponseDto.builder()
                .email(email)
                .loginType(loginType)
                .build();
    }
}
