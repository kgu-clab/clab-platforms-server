package page.clab.api.domain.product.application;

import page.clab.api.domain.product.dto.request.ProductUpdateRequestDto;

public interface UpdateProductService {
    Long execute(Long productId, ProductUpdateRequestDto requestDto);
}
