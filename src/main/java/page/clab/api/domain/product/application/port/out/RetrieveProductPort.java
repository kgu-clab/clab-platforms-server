package page.clab.api.domain.product.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.product.domain.Product;

import java.util.Optional;

public interface RetrieveProductPort {
    Optional<Product> findById(Long productId);
    Product findByIdOrThrow(Long productId);
    Page<Product> findAllByIsDeletedTrue(Pageable pageable);
    Page<Product> findByConditions(String productName, Pageable pageable);
}
