package page.clab.api.domain.product.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.product.application.ProductService;
import page.clab.api.domain.product.dto.request.ProductRequestDto;
import page.clab.api.domain.product.dto.request.ProductUpdateRequestDto;
import page.clab.api.domain.product.dto.response.ProductResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.dto.ResponseModel;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "Product", description = "서비스")
@Slf4j
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "[A] 서비스 등록", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping("")
    public ResponseModel createProduct(
            @Valid @RequestBody ProductRequestDto requestDto
    ) {
        Long id = productService.createProduct(requestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "[U] 서비스 조회", description = "ROLE_USER 이상의 권한이 필요함<br> " +
            "서비스명을 입력하지 않으면 전체 조회됨")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("")
    public ResponseModel getProductsByConditions(
            @RequestParam(required = false) String productName,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<ProductResponseDto> products = productService.getProductsByConditions(productName, pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(products);
        return responseModel;
    }

    @Operation(summary = "[A] 서비스 수정", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @PatchMapping("/{productId}")
    public ResponseModel updateProduct(
            @PathVariable(name = "productId") Long productId,
            @Valid @RequestBody ProductUpdateRequestDto requestDto
    ) {
        Long id = productService.updateProduct(productId, requestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "[A] 서비스 삭제", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @DeleteMapping("")
    public ResponseModel deleteProduct(
            @RequestParam Long productId
    ) {
        Long id = productService.deleteProduct(productId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

}
