package greeny.backend.domain.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Provider {
    KAKAO("Kakao"),
    NAVER("Naver");

    private final String name;
}