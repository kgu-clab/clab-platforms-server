package page.clab.api.domain.product.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductJpaEntity, Long>, ProductRepositoryCustom, QuerydslPredicateExecutor<ProductJpaEntity> {
    @Query(value = "SELECT p.* FROM product p WHERE p.is_deleted = true", nativeQuery = true)
    Page<ProductJpaEntity> findAllByIsDeletedTrue(Pageable pageable);
}
