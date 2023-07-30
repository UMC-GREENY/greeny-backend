package greeny.backend.domain.store.controller;

import greeny.backend.domain.bookmark.service.BookmarkService;
import greeny.backend.domain.member.service.MemberService;
import greeny.backend.domain.store.service.StoreService;
import greeny.backend.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static greeny.backend.response.Response.success;
import static greeny.backend.response.SuccessMessage.SUCCESS_TO_GET_SIMPLE_STORE_INFOS;
import static greeny.backend.response.SuccessMessage.SUCCESS_TO_GET_STORE_INFO;
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

    // 스토어 목록 조회 API
    @Operation(summary = "Get simple store infos API", description = "please get simple store infos.")
    @ResponseStatus(OK)
    @GetMapping("/simple")
    public Response getSimpleStoreInfos() {
        return success(SUCCESS_TO_GET_SIMPLE_STORE_INFOS, storeService.getSimpleStoreInfos());
    }

    // 인증된 사용자의 스토어 목록 조회 API
    @Operation(summary = "Get simple store infos with auth member API", description = "please get simple store infos.")
    @ResponseStatus(OK)
    @GetMapping("/auth/simple")
    public Response getSimpleStoreInfosWithAuthMember() {
        return success(
                SUCCESS_TO_GET_SIMPLE_STORE_INFOS,
                storeService.getSimpleStoreInfosWithAuthMember(bookmarkService.getStoreBookmarkInfos(memberService.getCurrentMember()))
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
