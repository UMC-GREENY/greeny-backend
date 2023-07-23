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

    private Long id;
    private String email;
    private String name;
    private String phone;
    private String birth;

    public static GetMemberInfoResponseDto toDto(Member member) {
        return GetMemberInfoResponseDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getMemberProfile().getName())
                .phone(member.getMemberProfile().getPhone())
                .birth(member.getMemberProfile().getBirth())
                .build();
    }

}
