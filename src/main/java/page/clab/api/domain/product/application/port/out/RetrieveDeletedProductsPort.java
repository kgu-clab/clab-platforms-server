package page.clab.api.domain.product.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.product.domain.Product;

public interface RetrieveDeletedProductsPort {
    Page<Product> findAllByIsDeletedTrue(Pageable pageable);
}
