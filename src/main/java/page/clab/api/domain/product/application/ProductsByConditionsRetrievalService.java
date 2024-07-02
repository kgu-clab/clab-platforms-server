package page.clab.api.domain.product.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.product.application.port.in.ProductsByConditionsRetrievalUseCase;
import page.clab.api.domain.product.application.port.out.RetrieveProductsByConditionsPort;
import page.clab.api.domain.product.domain.Product;
import page.clab.api.domain.product.dto.response.ProductResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class ProductsByConditionsRetrievalService implements ProductsByConditionsRetrievalUseCase {

    private final RetrieveProductsByConditionsPort retrieveProductsByConditionsPort;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<ProductResponseDto> retrieve(String productName, Pageable pageable) {
        Page<Product> products = retrieveProductsByConditionsPort.findByConditions(productName, pageable);
        return new PagedResponseDto<>(products.map(ProductResponseDto::toDto));
    }
}
