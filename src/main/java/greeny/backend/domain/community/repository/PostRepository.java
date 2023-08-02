package greeny.backend.domain.community.repository;

import greeny.backend.domain.community.entity.Post;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 페이징과 컬렉션 fetch join을 같이 사용 시 HHH000104 : 메모리과부하 경고 발생 -> 페이징할 때 컬렉션인 postFiles는 Lazy 로딩으로 가져오기
    @EntityGraph(attributePaths = {"writer"})
    @NotNull Page<Post> findAll(@NotNull Pageable pageable);

    @EntityGraph(attributePaths = {"writer"})
    Page<Post> findAllByWriterId(Long writerId, Pageable pageable);

    //제목 검색 -> 제목+내용 검색. IgnoreCase : 대소문자 구별 무시
    @EntityGraph(attributePaths = {"writer"})
    Page<Post> findAllByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String title, String content, Pageable pageable);

    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.writer WHERE p.id = :id")
    Optional<Post> findByIdWithWriter(@Param("id") Long id);

    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.writer LEFT JOIN FETCH p.postFiles WHERE p.id = :id")
    Optional<Post> findByIdWithWriterAndPostFiles(@Param("id") Long id);

    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.writer LEFT JOIN FETCH p.postFiles LEFT JOIN FETCH p.postLikes WHERE p.id = :id")
    Optional<Post> findByIdWithWriterAndPostFilesAndPostLikes(@Param("id") Long id);
}
