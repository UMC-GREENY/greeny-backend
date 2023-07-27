package greeny.backend.domain.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Provider {
    KAKAO("Kakao"),
    NAVER("Naver");

    private final String name;
}
