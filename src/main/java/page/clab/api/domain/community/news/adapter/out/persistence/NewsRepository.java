package page.clab.api.domain.community.news.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<NewsJpaEntity, Long>, NewsRepositoryCustom, QuerydslPredicateExecutor<NewsJpaEntity> {
    @Query(value = "SELECT n.* FROM news n WHERE n.is_deleted = true", nativeQuery = true)
    Page<NewsJpaEntity> findAllByIsDeletedTrue(Pageable pageable);
}
