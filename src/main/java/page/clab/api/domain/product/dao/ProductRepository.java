package page.clab.api.domain.product.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.product.domain.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom, QuerydslPredicateExecutor<Product> {

    Page<Product> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Query(value = "SELECT p.* FROM product p WHERE p.is_deleted = true", nativeQuery = true)
    Page<Product> findAllByIsDeletedTrue(Pageable pageable);

}
