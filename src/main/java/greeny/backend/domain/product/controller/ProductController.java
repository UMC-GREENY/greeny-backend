package greeny.backend.domain.product.controller;

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
import static greeny.backend.response.SuccessMessage.SUCCESS_TO_GET_SIMPLE_PRODUCT_INFOS;
import static org.springframework.http.HttpStatus.OK;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
@Tag(name = "Product", description = "Product API Document")
public class ProductController {

    private final ProductService productService;

    //제품 목록 API
    @Operation(summary = "Get simple product infos API", description = "please get product store infos.")
    @ResponseStatus(OK)
    @GetMapping("/simple")
    public Response getSimpleProductInfos(){
        return success(SUCCESS_TO_GET_SIMPLE_PRODUCT_INFOS, productService.getSimpleProductInfos());
    }
}
