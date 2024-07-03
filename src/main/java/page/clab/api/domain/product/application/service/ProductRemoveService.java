package page.clab.api.domain.product.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.product.application.port.in.RemoveProductUseCase;
import page.clab.api.domain.product.application.port.out.LoadProductPort;
import page.clab.api.domain.product.application.port.out.UpdateProductPort;
import page.clab.api.domain.product.domain.Product;

@Service
@RequiredArgsConstructor
public class ProductRemoveService implements RemoveProductUseCase {

    private final LoadProductPort loadProductPort;
    private final UpdateProductPort updateProductPort;

    @Transactional
    @Override
    public Long remove(Long productId) {
        Product product = loadProductPort.findByIdOrThrow(productId);
        product.delete();
        return updateProductPort.update(product).getId();
    }
}
