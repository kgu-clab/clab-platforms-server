package page.clab.api.domain.product.application.port.in;

import page.clab.api.domain.product.dto.request.ProductRequestDto;

public interface ProductRegisterUseCase {
    Long register(ProductRequestDto requestDto);
}
