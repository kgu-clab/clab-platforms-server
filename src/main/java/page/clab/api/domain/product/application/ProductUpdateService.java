package page.clab.api.domain.product.application;

import page.clab.api.domain.product.dto.request.ProductUpdateRequestDto;

public interface ProductUpdateService {
    Long update(Long productId, ProductUpdateRequestDto requestDto);
}
