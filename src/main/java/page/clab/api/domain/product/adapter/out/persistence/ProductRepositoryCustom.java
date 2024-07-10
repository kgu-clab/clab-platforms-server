package page.clab.api.domain.product.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {
    Page<ProductJpaEntity> findByConditions(String productName, Pageable pageable);
}
