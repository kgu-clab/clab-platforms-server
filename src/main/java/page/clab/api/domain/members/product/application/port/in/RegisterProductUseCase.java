package page.clab.api.domain.members.product.application.port.in;

import page.clab.api.domain.members.product.application.dto.request.ProductRequestDto;

public interface RegisterProductUseCase {

    Long registerProduct(ProductRequestDto requestDto);
}
