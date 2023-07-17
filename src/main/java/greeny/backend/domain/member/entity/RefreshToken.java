package greeny.backend.domain.member.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshToken {
    @Id
    @Column(name = "rt_key")
    private String key;
    @Column(name = "rt_value")
    private String value;

    public RefreshToken updateValue(String refreshToken) {
        this.value = refreshToken;
        return this;
    }
}
