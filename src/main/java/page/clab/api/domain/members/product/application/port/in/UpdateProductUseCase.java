package page.clab.api.domain.members.product.application.port.in;

import page.clab.api.domain.members.product.application.dto.request.ProductUpdateRequestDto;

public interface UpdateProductUseCase {

    Long updateProduct(Long productId, ProductUpdateRequestDto requestDto);
}
