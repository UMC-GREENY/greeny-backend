package greeny.backend.domain.store.controller;

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

import static greeny.backend.response.Response.*;
import static greeny.backend.response.SuccessMessage.*;
import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores")
@Tag(name = "Store", description = "Store API Document")
public class StoreController {

    private final StoreService storeService;

    @Operation(summary = "Get simple store infos API", description = "please get simple store infos.")
    @ResponseStatus(OK)
    @GetMapping("/simple")
    public Response getSimpleStoreInfosWithReview() {
        return success(SUCCESS_TO_GET_SIMPLE_STORE_INFOS, storeService.getSimpleStoreInfosWithReview());
    }


}
