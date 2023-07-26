package greeny.backend.domain.bookmark.service;

import greeny.backend.domain.bookmark.entity.StoreBookmark;
import greeny.backend.domain.bookmark.repository.StoreBookmarkRepository;
import greeny.backend.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookmarkService {

    private final StoreBookmarkRepository storeBookmarkRepository;

    public List<StoreBookmark> getMyStoreBookmarkInfos(Member liker) {  // 현재 사용자의 찜한 스토어 목록 가져오기
        return storeBookmarkRepository.findStoreBookmarksByLiker(liker);
    }
}
