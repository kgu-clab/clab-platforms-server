package page.clab.api.domain.members.product.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.members.product.application.port.in.RemoveProductUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "Members - Product", description = "동아리 운영 서비스")
public class ProductRemoveController {

    private final RemoveProductUseCase removeProductUseCase;

    @Operation(summary = "[A] 서비스 삭제", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("")
    public ApiResponse<Long> removeProduct(
            @RequestParam Long productId
    ) {
        Long id = removeProductUseCase.removeProduct(productId);
        return ApiResponse.success(id);
    }
}
