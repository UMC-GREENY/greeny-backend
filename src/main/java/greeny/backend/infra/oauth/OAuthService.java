package greeny.backend.infra.oauth;

import greeny.backend.domain.member.presentation.dto.sign.social.KakaoMemberInfoDto;
import greeny.backend.domain.member.presentation.dto.sign.social.NaverMemberInfoDto;
import greeny.backend.domain.member.presentation.dto.sign.social.SocialTokenDto;
import greeny.backend.exception.situation.EmptySocialTokenInfoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuthService {

    private final RestTemplate restTemplate;
    private static final String GRANT_TYPE = "authorization_code";

    @Value("${oauth.kakao.client-id}")
    private String kakaoClientId;

    @Value("${oauth.naver.client-id}")
    private String naverClientId;

    @Value("${oauth.naver.secret}")
    private String naverClientSecret;

    public KakaoMemberInfoDto requestToKakao(String authorizationCode) {
        return requestMemberInfoToKakao(requestToken(authorizationCode));
    }

    public NaverMemberInfoDto requestToNaver(String authorizationCode, String state) {
        return requestMemberInfoToNaver(requestToken(authorizationCode, state));
    }

    private String requestToken(String authorizationCode) {

        String url = "https://kauth.kakao.com/oauth/token";

        SocialTokenDto socialTokenDto = restTemplate.postForObject(
                url,
                new HttpEntity<>(makeBody(authorizationCode), makeContentType()),
                SocialTokenDto.class
        );

        if (socialTokenDto == null) {
            throw new EmptySocialTokenInfoException();
        }

        return socialTokenDto.getAccessToken();
    }

    private String requestToken(String authorizationCode, String state) {

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

    private KakaoMemberInfoDto requestMemberInfoToKakao(String accessToken) {

        String url = "https://kapi.kakao.com/v2/user/me";
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("property_keys", "[\"kakao_account.email\"]");

        return restTemplate.postForObject(
                url,
                new HttpEntity<>(body, makeHeaderWithAccessToken(accessToken)),
                KakaoMemberInfoDto.class
        );
    }

    private NaverMemberInfoDto requestMemberInfoToNaver(String accessToken) {

        String url = "https://openapi.naver.com/v1/nid/me";

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        return restTemplate.postForObject(
                url,
                new HttpEntity<>(body, makeHeaderWithAccessToken(accessToken)),
                NaverMemberInfoDto.class
        );
    }

    private HttpHeaders makeContentType() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return httpHeaders;
    }
    private HttpHeaders makeHeaderWithAccessToken(String accessToken) {
        HttpHeaders httpHeaders = makeContentType();
        httpHeaders.set("Authorization", "Bearer " + accessToken);
        return httpHeaders;
    }
    private MultiValueMap<String, String> makeBody(String authorizationCode) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        body.add("code", authorizationCode);
        body.add("grant_type", GRANT_TYPE);
        body.add("client_id", kakaoClientId);

        return body;
    }
    private MultiValueMap<String, String> makeBody(String authorizationCode, String state) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        body.add("code", authorizationCode);
        body.add("state", state);
        body.add("grant_type", GRANT_TYPE);
        body.add("client_id", naverClientId);
        body.add("client_secret", naverClientSecret);

        return body;
    }
}