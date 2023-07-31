package greeny.backend.domain.product.controller;

import greeny.backend.domain.bookmark.service.BookmarkService;
import greeny.backend.domain.member.service.MemberService;
import greeny.backend.domain.product.service.ProductService;
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
import static greeny.backend.response.SuccessMessage.SUCCESS_TO_GET_PRODUCT_INFO;
import static greeny.backend.response.SuccessMessage.SUCCESS_TO_GET_SIMPLE_PRODUCT_INFOS;
import static org.springframework.http.HttpStatus.OK;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
@Tag(name = "Product", description = "Product API Document")
public class ProductController {

    private final ProductService productService;
    private final BookmarkService bookmarkService;
    private final MemberService memberService;

    // 모든 사용자의 제품 목록 조회 API
    @Operation(summary = "Get simple product infos API", description = "please get product store infos.")
    @ResponseStatus(OK)
    @GetMapping("/simple")
    public Response getSimpleProductInfos(){
        return success(SUCCESS_TO_GET_SIMPLE_PRODUCT_INFOS, productService.getSimpleProductInfos());
    }

    // 인증된 사용자의 제품 목록 조회 API
    @Operation(summary = "Get simple product infos with auth member API", description = "please get product store infos.")
    @ResponseStatus(OK)
    @GetMapping("/auth/simple")
    public Response getSimpleProductInfosWithAuthMember(){
        return success(
                SUCCESS_TO_GET_SIMPLE_PRODUCT_INFOS,
                productService.getSimpleProductInfosWithAuthMember(bookmarkService.getProductBookmarks(memberService.getCurrentMember()))
        );
    }

    // 제품 상세 목록 조회 PI
    @Operation(summary = "Get product info API", description = "put product id what you want to see.")
    @ResponseStatus(OK)
    @GetMapping()
    public Response getProductInfo(Long productId){
        return success(SUCCESS_TO_GET_PRODUCT_INFO, productService.getProductInfo(productId));
    }
}
