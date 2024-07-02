package page.clab.api.domain.product.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.product.domain.Product;

public interface RetrieveProductsByConditionsPort {
    Page<Product> findByConditions(String productName, Pageable pageable);
}
