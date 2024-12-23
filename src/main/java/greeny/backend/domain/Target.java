package greeny.backend.domain;

import lombok.Getter;

@Getter
public enum Target {
    STORE,
    PRODUCT;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}