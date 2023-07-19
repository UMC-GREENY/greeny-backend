package greeny.backend.domain.post.repository;

import greeny.backend.domain.post.entity.PostFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostFileRepository extends JpaRepository<PostFile, Long> {
}
