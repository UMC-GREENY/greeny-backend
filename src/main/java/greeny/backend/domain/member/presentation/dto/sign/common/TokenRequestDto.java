package greeny.backend.domain.member.presentation.dto.sign.common;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class TokenRequestDto {
    private String accessToken;
    private String refreshToken;
}