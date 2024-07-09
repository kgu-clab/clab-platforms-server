package page.clab.api.domain.product.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.product.domain.Product;

public interface ProductRepositoryCustom {
    Page<Product> findByConditions(String productName, Pageable pageable);
}
