package page.clab.api.domain.members.product.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.members.product.application.dto.request.ProductRequestDto;
import page.clab.api.domain.members.product.application.port.in.RegisterProductUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "Members - Product", description = "동아리 운영 서비스")
public class ProductRegisterController {

    private final RegisterProductUseCase registerProductUseCase;

    @Operation(summary = "[A] 서비스 등록", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({ "ROLE_ADMIN", "ROLE_SUPER" })
    @PostMapping("")
    public ApiResponse<Long> registerProduct(
            @Valid @RequestBody ProductRequestDto requestDto
    ) {
        Long id = registerProductUseCase.registerProduct(requestDto);
        return ApiResponse.success(id);
    }
}
