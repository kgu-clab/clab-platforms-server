package page.clab.api.domain.product.application.port.in;

import page.clab.api.domain.product.dto.request.ProductUpdateRequestDto;

public interface ProductUpdateUseCase {
    Long update(Long productId, ProductUpdateRequestDto requestDto);
}
