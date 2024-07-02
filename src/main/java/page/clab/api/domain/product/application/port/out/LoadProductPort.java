package page.clab.api.domain.product.application.port.out;

import page.clab.api.domain.product.domain.Product;

import java.util.Optional;

public interface LoadProductPort {
    Optional<Product> findById(Long productId);
    Product findByIdOrThrow(Long productId);
}
