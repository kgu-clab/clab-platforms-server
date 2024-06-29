package page.clab.api.domain.product.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.product.application.DeletedProductsRetrievalService;
import page.clab.api.domain.product.dao.ProductRepository;
import page.clab.api.domain.product.domain.Product;
import page.clab.api.domain.product.dto.response.ProductResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class DeletedProductsRetrievalServiceImpl implements DeletedProductsRetrievalService {

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<ProductResponseDto> retrieveDeleted(Pageable pageable) {
        Page<Product> products = productRepository.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(products.map(ProductResponseDto::toDto));
    }
}
