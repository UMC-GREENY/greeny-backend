package greeny.backend.domain.bookmark.entity;

import greeny.backend.domain.AuditEntity;
import greeny.backend.domain.member.entity.Member;
import greeny.backend.domain.store.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreBookmark extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_bookmark_id")
    private Long id;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "store_id")
    private Store store;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "liker_id")
    private Member liker;
}
