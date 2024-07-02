package page.clab.api.domain.product.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.product.application.ProductUpdateUseCase;
import page.clab.api.domain.product.dto.request.ProductUpdateRequestDto;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "Product", description = "서비스")
public class ProductUpdateController {

    private final ProductUpdateUseCase productUpdateUseCase;

    @Operation(summary = "[A] 서비스 수정", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @PatchMapping("/{productId}")
    public ApiResponse<Long> updateProduct(
            @PathVariable(name = "productId") Long productId,
            @Valid @RequestBody ProductUpdateRequestDto requestDto
    ) {
        Long id = productUpdateUseCase.update(productId, requestDto);
        return ApiResponse.success(id);
    }
}
