package page.clab.api.global.common.softDelete;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.product.dto.request.ProductRequestDto;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/softDelete")
@RequiredArgsConstructor
@Tag(name = "SoftDelete", description = "소프트 딜리트")
public class SoftDeleteController {

    private final SoftDeleteService softDeleteService;

/*    @Operation(summary = "[S] 소프트 딜리트 된 ", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping("")
    public ApiResponse<Long> createProduct(
            @Valid @RequestBody ProductRequestDto requestDto
    ) {
        Long id = productService.createProduct(requestDto);
        return ApiResponse.success(id);
    }*/

}
