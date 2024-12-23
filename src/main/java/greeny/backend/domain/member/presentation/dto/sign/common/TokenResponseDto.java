package greeny.backend.domain.member.presentation.dto.sign.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TokenResponseDto {

    private String accessToken;
    private String refreshToken;
    private String email;

    public static TokenResponseDto excludeEmailInDto(String accessToken, String refreshToken) {
        return TokenResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public static TokenResponseDto from(String accessToken, String refreshToken) {
        return TokenResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .email("nothing")
                .build();
    }

    public static TokenResponseDto excludeTokenInDto(String email) {
        return TokenResponseDto.builder()
                .email(email)
                .build();
    }
}