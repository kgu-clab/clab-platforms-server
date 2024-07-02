package page.clab.api.domain.product.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.product.application.port.in.DeletedProductsRetrievalUseCase;
import page.clab.api.domain.product.application.port.out.RetrieveDeletedProductsPort;
import page.clab.api.domain.product.domain.Product;
import page.clab.api.domain.product.dto.response.ProductResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class DeletedProductsRetrievalService implements DeletedProductsRetrievalUseCase {

    private final RetrieveDeletedProductsPort retrieveDeletedProductsPort;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<ProductResponseDto> retrieve(Pageable pageable) {
        Page<Product> products = retrieveDeletedProductsPort.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(products.map(ProductResponseDto::toDto));
    }
}
