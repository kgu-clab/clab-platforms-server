package page.clab.api.domain.product.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.product.application.ProductsByConditionsRetrievalService;
import page.clab.api.domain.product.dao.ProductRepository;
import page.clab.api.domain.product.domain.Product;
import page.clab.api.domain.product.dto.response.ProductResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class ProductsByConditionsRetrievalServiceImpl implements ProductsByConditionsRetrievalService {

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<ProductResponseDto> retrieveByConditions(String productName, Pageable pageable) {
        Page<Product> products = productRepository.findByConditions(productName, pageable);
        return new PagedResponseDto<>(products.map(ProductResponseDto::toDto));
    }
}
