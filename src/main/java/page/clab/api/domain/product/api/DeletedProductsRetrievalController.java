package page.clab.api.domain.product.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.product.application.port.in.DeletedProductsRetrievalUseCase;
import page.clab.api.domain.product.dto.response.ProductResponseDto;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.dto.PagedResponseDto;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "Product", description = "서비스")
public class DeletedProductsRetrievalController {

    private final DeletedProductsRetrievalUseCase deletedProductsRetrievalUseCase;

    @Operation(summary = "[S] 삭제된 서비스 조회하기", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    @GetMapping("/deleted")
    public ApiResponse<PagedResponseDto<ProductResponseDto>> retrieveDeletedProducts(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<ProductResponseDto> products = deletedProductsRetrievalUseCase.retrieve(pageable);
        return ApiResponse.success(products);
    }
}
