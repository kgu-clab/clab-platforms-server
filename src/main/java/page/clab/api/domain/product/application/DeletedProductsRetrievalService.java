package page.clab.api.domain.product.application;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.product.dto.response.ProductResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface DeletedProductsRetrievalService {
    PagedResponseDto<ProductResponseDto> retrieve(Pageable pageable);
}
