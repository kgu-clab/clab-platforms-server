package page.clab.api.domain.product.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.product.application.dto.request.ProductUpdateRequestDto;
import page.clab.api.domain.product.application.port.in.UpdateProductUseCase;
import page.clab.api.domain.product.application.port.out.RetrieveProductPort;
import page.clab.api.domain.product.application.port.out.UpdateProductPort;
import page.clab.api.domain.product.domain.Product;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class ProductUpdateService implements UpdateProductUseCase {

    private final ValidationService validationService;
    private final RetrieveProductPort retrieveProductPort;
    private final UpdateProductPort updateProductPort;

    @Transactional
    @Override
    public Long updateProduct(Long productId, ProductUpdateRequestDto requestDto) {
        Product product = retrieveProductPort.findByIdOrThrow(productId);
        product.update(requestDto);
        validationService.checkValid(product);
        return updateProductPort.update(product).getId();
    }
}
