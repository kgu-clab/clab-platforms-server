package page.clab.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.service.ProductService;
import page.clab.api.type.dto.ProductRequestDto;
import page.clab.api.type.dto.ProductResponseDto;
import page.clab.api.type.dto.ResponseModel;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "Product")
@Slf4j
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "서비스 등록", description = "서비스 등록")
    @PostMapping("")
    public ResponseModel createProduct(
            @Valid @RequestBody ProductRequestDto productRequestDto,
            BindingResult result
    ) throws MethodArgumentNotValidException, PermissionDeniedException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        productService.createProduct(productRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "서비스 조회", description = "서비스 조회")
    @GetMapping("")
    public ResponseModel getProducts() {
        List<ProductResponseDto> productResponseDtos = productService.getProducts();
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(productResponseDtos);
        return responseModel;
    }

    @Operation(summary = "서비스 검색", description = "서비스의 이름을 기반으로 검색")
    @GetMapping("/search")
    public ResponseModel searchProduct(
            @RequestParam String productName
    ) {
        List<ProductResponseDto> productResponseDtos = productService.searchProduct(productName);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(productResponseDtos);
        return responseModel;
    }

    @Operation(summary = "서비스 수정", description = "서비스 수정")
    @PatchMapping("/{productId}")
    public ResponseModel updateProduct(
            @PathVariable Long productId,
            @Valid @RequestBody ProductRequestDto productRequestDto,
            BindingResult result
    ) throws MethodArgumentNotValidException, PermissionDeniedException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        productService.updateProduct(productId, productRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "서비스 삭제", description = "서비스 삭제")
    @DeleteMapping("")
    public ResponseModel deleteProduct(
            @RequestParam Long productId
    ) throws PermissionDeniedException {
        productService.deleteProduct(productId);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

}
