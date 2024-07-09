package page.clab.api.domain.product.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.product.application.dto.response.ProductResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface RetrieveDeletedProductsUseCase {
    PagedResponseDto<ProductResponseDto> retrieveDeletedProducts(Pageable pageable);
}
