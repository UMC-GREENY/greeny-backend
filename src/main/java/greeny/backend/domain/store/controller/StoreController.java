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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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

    // 모든 사용자의 타입에 따른 스토어 목록 조회 API (Type -> new, best) -> 리스트 반환
    @Operation(summary = "Get new or best simple store infos API", description = "put your type info what you want to see.")
    @ResponseStatus(OK)
    @GetMapping("/new-best/simple")
    public Response getNewOrBestSimpleStoreInfos(String type) {
        return success(SUCCESS_TO_GET_NEW_OR_BEST_SIMPLE_STORE_INFOS, storeService.getNewOrBestSimpleStoreInfos(type));
    }

    // 인증된 사용자의 타입에 따른 스토어 목록 조회 API (Type -> new, best) -> 리스트 반환
    @Operation(summary = "Get new or best simple store infos with auth member API", description = "put your type info what you want to see.")
    @ResponseStatus(OK)
    @GetMapping("/auth/new-best/simple")
    public Response getNewOrBestSimpleStoreInfosWithAuthMember(String type) {
        return success(
                SUCCESS_TO_GET_NEW_OR_BEST_SIMPLE_STORE_INFOS,
                storeService.getNewOrBestSimpleStoreInfoWithAuthMember(type, bookmarkService.getStoreBookmarks(memberService.getCurrentMember()))
        );
    }

    // 모든 사용자의 정렬 타입에 따른 스토어 목록 조회 API (Type -> bookmark, review) -> 페이지 반환
    @Operation(summary = "Get simple store infos sorted by bookmark or review", description = "put your page and type info what you want to see.")
    @ResponseStatus(OK)
    @GetMapping("/sort/simple")
    public Response getSortedSimpleStoreInfos(String type, @ParameterObject Pageable pageable) {
        return success(SUCCESS_TO_GET_SORTED_SIMPLE_STORE_INFOS, storeService.getSortedSimpleStoreInfos(type, pageable));
    }

    // 스토어 상세 정보 API
    @Operation(summary = "Get store info API", description = "put store id what you want to see.")
    @ResponseStatus(OK)
    @GetMapping()
    public Response getStoreInfo(Long storeId) {
        return success(SUCCESS_TO_GET_STORE_INFO, storeService.getStoreInfo(storeId));
    }
}
