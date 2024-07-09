package page.clab.api.domain.product.application.port.in;

import page.clab.api.domain.product.application.dto.request.ProductRequestDto;

public interface RegisterProductUseCase {
    Long registerProduct(ProductRequestDto requestDto);
}
