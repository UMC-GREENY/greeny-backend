package greeny.backend.domain.bookmark.controller;

import greeny.backend.domain.bookmark.service.BookmarkService;
import greeny.backend.domain.member.service.MemberService;
import greeny.backend.domain.product.service.ProductService;
import greeny.backend.domain.store.service.StoreService;
import greeny.backend.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static greeny.backend.response.Response.success;
import static greeny.backend.response.SuccessMessage.SUCCESS_TO_ADD_BOOKMARK;
import static greeny.backend.response.SuccessMessage.SUCCESS_TO_GET_SIMPLE_STORE_INFOS;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookmarks")
@Tag(name = "Bookmark", description = "Bookmark API Document")
public class BookmarkController {

    private final MemberService memberService;
    private final StoreService storeService;
    private final ProductService productService;
    private final BookmarkService bookmarkService;

    // 스토어 or 제품 찜하기 API
    @Operation(summary = "Add store or product bookmark API", description = "put type info and store or product id what you want to bookmark.")
    @ResponseStatus(OK)
    @PostMapping()
    public Response addBookmark(String type, Long id) {
        bookmarkService.addBookmark(type, storeService.getStore(id), productService.getProduct(id), memberService.getCurrentMember());
        return success(SUCCESS_TO_ADD_BOOKMARK);
    }


}
