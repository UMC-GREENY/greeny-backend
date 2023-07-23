package greeny.backend.domain.member.dto.sign.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)  // NULL 인 속성은 제외
public class TokenResponseDto {

    private String accessToken;
    private String refreshToken;
    private String email;  // 소셜 로그인의 경우 최초 로그인인지 판단

    public static TokenResponseDto excludeEmailInDto(String accessToken, String refreshToken) {  // Builder 를 이용한 email 을 제외하고 TokenResponseDto 객체 생성, 일반 로그인
        return TokenResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public static TokenResponseDto from(String accessToken, String refreshToken) {  // Builder 를 이용한 TokenResponseDto 객체 생성, 소셜 로그인이 처음이 아닐 경우
        return TokenResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .email("nothing")
                .build();
    }

    public static TokenResponseDto excludeTokenInDto(String email) {  // Builder 를 이용한 access, refresh token 을 제외하고 TokenResponseDto 객체 생성, 소셜 로그인이 처음일 경우
        return TokenResponseDto.builder()
                .email(email)
                .build();
    }
}
