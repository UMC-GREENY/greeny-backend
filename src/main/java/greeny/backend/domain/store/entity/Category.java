package greeny.backend.domain.store.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    REFILL("리필샵"),
    DAILY("친환경생필품"),
    CAFE("카페"),
    RESTAURANT("식당"),
    ETC("기타");

    private final String name;
}
