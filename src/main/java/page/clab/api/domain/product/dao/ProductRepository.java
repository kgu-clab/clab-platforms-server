package page.clab.api.domain.product.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.product.domain.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findAllByNameContainingOrderByCreatedAtDesc(String productName, Pageable pageable);

    Page<Product> findAllByOrderByCreatedAtDesc(Pageable pageable);

}
