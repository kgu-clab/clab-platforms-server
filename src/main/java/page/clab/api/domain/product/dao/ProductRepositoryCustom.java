package page.clab.api.domain.product.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.product.domain.Product;

public interface ProductRepositoryCustom {
    Page<Product> findByConditions(String productName, Pageable pageable);
}
