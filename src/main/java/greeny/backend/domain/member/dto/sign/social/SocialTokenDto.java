package greeny.backend.domain.member.dto.sign.social;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)  // Kakao or Naver 에서 응답하는 데이터들 중 'access_token'만 받기 위해
public class SocialTokenDto {  // Kakao or Naver 에서 토큰을 받아오는 dto
    @JsonProperty("access_token")  // Kakao or Naver 에서 지정한 응답 프로퍼티 이름
    private String accessToken;
}
