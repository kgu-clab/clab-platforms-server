package page.clab.api.domain.product.application;

import page.clab.api.domain.product.dto.request.ProductRequestDto;

public interface ProductRegisterService {
    Long register(ProductRequestDto requestDto);
}
