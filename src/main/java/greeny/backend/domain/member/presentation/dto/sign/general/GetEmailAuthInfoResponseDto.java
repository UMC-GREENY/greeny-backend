package greeny.backend.domain.member.presentation.dto.sign.general;

import lombok.*;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class GetEmailAuthInfoResponseDto {
    private String email;
    private String token;
}