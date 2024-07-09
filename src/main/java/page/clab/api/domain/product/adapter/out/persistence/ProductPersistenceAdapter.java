package page.clab.api.domain.product.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import page.clab.api.domain.product.application.port.out.RegisterProductPort;
import page.clab.api.domain.product.application.port.out.RetrieveProductPort;
import page.clab.api.domain.product.application.port.out.UpdateProductPort;
import page.clab.api.domain.product.domain.Product;
import page.clab.api.global.exception.NotFoundException;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductPersistenceAdapter implements
        RegisterProductPort,
        UpdateProductPort,
        RetrieveProductPort {

    private final ProductRepository repository;

    @Override
    public Product save(Product product) {
        return repository.save(product);
    }

    @Override
    public Product update(Product product) {
        return repository.save(product);
    }

    @Override
    public Optional<Product> findById(Long productId) {
        return repository.findById(productId);
    }

    @Override
    public Product findByIdOrThrow(Long productId) {
        return repository.findById(productId)
                .orElseThrow(() -> new NotFoundException("[Product] id: " + productId + "에 해당하는 상품이 존재하지 않습니다."));
    }

    @Override
    public Page<Product> findAllByIsDeletedTrue(Pageable pageable) {
        return repository.findAllByIsDeletedTrue(pageable);
    }

    @Override
    public Page<Product> findByConditions(String productName, Pageable pageable) {
        return repository.findByConditions(productName, pageable);
    }
}
