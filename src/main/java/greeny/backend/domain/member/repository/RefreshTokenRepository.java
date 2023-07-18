package greeny.backend.domain.member.repository;

import greeny.backend.domain.member.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    RefreshToken findByKey(String key);
    boolean existsByKey(String key);
}
