package greeny.backend.domain.member.presentation.dto.sign.social;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NaverMemberInfoDto {
    @JsonProperty("response")
    private Response response;

    @Getter
    public static class Response {
        private String email;
    }
}