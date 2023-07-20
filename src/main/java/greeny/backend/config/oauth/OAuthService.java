package greeny.backend.config.oauth;

import greeny.backend.domain.member.dto.sign.social.KakaoMemberInfoDto;
import greeny.backend.domain.member.dto.sign.social.NaverMemberInfoDto;
import greeny.backend.domain.member.dto.sign.social.SocialTokenDto;
import greeny.backend.exception.situation.EmptySocialTokenInfoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuthService {  // Kakao or Naver 에 토큰, 사용자 프로필 정보를 요청

    private final RestTemplate restTemplate;
    private static final String GRANT_TYPE = "authorization_code";

    // application.yml 에 설정된 데이터를 변수에 매칭
    @Value("${oauth.kakao.client-id}")
    private String kakaoClientId;
    @Value("${oauth.naver.client-id}")
    private String naverClientId;
    @Value("${oauth.naver.secret}")
    private String naverClientSecret;

    public String requestToken(String authorizationCode) {  // Kakao 에 토큰 요청

        String url = "https://kauth.kakao.com/oauth/token";

        SocialTokenDto socialTokenDto = restTemplate.postForObject(  // POST 메소드로 makeBody() 메소드로 만들어진 body 가 쿼리 파라미터 형식으로 변경되고 url 에 추가됨
                url,
                new HttpEntity<>(makeBody(authorizationCode), makeContentType()),
                SocialTokenDto.class  // 응답 받는 dto
        );

        if (socialTokenDto == null) {
            throw new EmptySocialTokenInfoException();
        }

        return socialTokenDto.getAccessToken();
    }

    public String requestToken(String authorizationCode, String state) {  // Naver 에 토큰 요청

        String url = "https://nid.naver.com/oauth2.0/token";

        SocialTokenDto socialTokenDto = restTemplate.postForObject(
                url,
                new HttpEntity<>(makeBody(authorizationCode, state), makeContentType()),
                SocialTokenDto.class
        );

        if (socialTokenDto == null) {
            throw new EmptySocialTokenInfoException();
        }

        return socialTokenDto.getAccessToken();
    }

    public KakaoMemberInfoDto requestMemberInfoToKakao(String accessToken) {  // Kakao 에 사용자 프로필 정보 요청

        String url = "https://kapi.kakao.com/v2/user/me";

        Map<String, String> body = new HashMap<>();
        body.put("property_keys", "[\"kakao_account.email\"]");

        return restTemplate.postForObject(
                url,
                new HttpEntity<>(body, makeHeaderWithAccessToken(accessToken)),
                KakaoMemberInfoDto.class
        );
    }

    public NaverMemberInfoDto requestMemberInfoToNaver(String accessToken) {  // Naver 에 사용자 프로필 정보 요청

        String url = "https://openapi.naver.com/v1/nid/me";

        Map<String, String> body = new HashMap<>();

        return restTemplate.postForObject(
                url,
                new HttpEntity<>(body, makeHeaderWithAccessToken(accessToken)),
                NaverMemberInfoDto.class
        );
    }

    private HttpHeaders makeContentType() {  // Kakao or Naver 에 요청 시 request header content-type 구성
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);  // url 형식으로 변경
        return httpHeaders;
    }
    private HttpHeaders makeHeaderWithAccessToken(String accessToken) {  // Kakao or Naver 에 요청 시 request header 에 쿼리 파라미터 형식인 accessToken 추가
        HttpHeaders httpHeaders = makeContentType();
        httpHeaders.set("Authorization", "Bearer " + accessToken);
        return httpHeaders;
    }
    private Map<String, String> makeBody(String authorizationCode) {  // Kakao 에 요청 시 url 에 담는 데이터 구성
        Map<String, String> body = new HashMap<>();

        body.put("code", authorizationCode);
        body.put("grant_type", GRANT_TYPE);
        body.put("client_id", kakaoClientId);

        return body;
    }
    private Map<String, String> makeBody(String authorizationCode, String state) {  // Naver 에 요청 시 url 에 담는 데이터 구성
        Map<String, String> body = new HashMap<>();

        body.put("code", authorizationCode);
        body.put("state", state);
        body.put("grant_type", GRANT_TYPE);
        body.put("client_id", naverClientId);
        body.put("client_secret", naverClientSecret);

        return body;
    }
}
