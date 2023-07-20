package greeny.backend.domain.member.dto.sign.social;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoMemberInfoDto {  // Kakao 에서 제공하는 사용자 프로필 정보를 받아오는 dto

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @Getter
    private class KakaoAccount {
        private String email;
    }
}
