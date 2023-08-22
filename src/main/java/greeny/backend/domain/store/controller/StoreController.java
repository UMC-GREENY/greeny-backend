package greeny.backend.domain.store.controller;

import greeny.backend.domain.bookmark.service.BookmarkService;
import greeny.backend.domain.member.service.MemberService;
import greeny.backend.domain.store.service.StoreService;
import greeny.backend.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import static greeny.backend.response.Response.success;
import static greeny.backend.response.SuccessMessage.*;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores")
@Tag(name = "Store", description = "Store API Document")
public class StoreController {

    private final StoreService storeService;
    private final BookmarkService bookmarkService;
    private final MemberService memberService;

    // 모든 사용자의 스토어 목록 불러오기 API (new, best, all, search, sort by bookmarks & reviews)
    @Operation(summary = "Get simple store infos API", description = "put keyword if you want to search and page info what you want to see.")
    @ResponseStatus(OK)
    @GetMapping("/simple")
    public Response getSimpleStoreInfos(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String category,
            @ParameterObject Pageable pageable
    ) {
        return success(SUCCESS_TO_GET_SIMPLE_STORE_INFOS, storeService.getSimpleStoreInfos(keyword, location, category, pageable));
    }

    // 인증된 사용자의 스토어 목록 불러오기 API (new, best, all, search, sort by bookmarks & reviews)
    @Operation(summary = "Get simple store infos with auth member API", description = "put keyword if you want to search and page info what you want to see.")
    @ResponseStatus(OK)
    @GetMapping("/auth/simple")
    public Response getSimpleStoreInfosWithAuthMember(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String category,
            @ParameterObject Pageable pageable
    ) {
        return success(
                SUCCESS_TO_GET_SIMPLE_STORE_INFOS,
                storeService.getSimpleStoreInfoWithAuthMember(
                        keyword,
                        bookmarkService.getStoreBookmarks(memberService.getCurrentMember()),
                        pageable
                )
        );
    }

    // 스토어 상세 정보 API
    @Operation(summary = "Get store info API", description = "put store id what you want to see.")
    @ResponseStatus(OK)
    @GetMapping()
    public Response getStoreInfo(Long storeId) {
        return success(SUCCESS_TO_GET_STORE_INFO, storeService.getStoreInfo(storeId));
    }
}