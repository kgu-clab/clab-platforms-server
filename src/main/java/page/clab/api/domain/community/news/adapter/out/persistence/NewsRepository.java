package page.clab.api.domain.community.news.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<NewsJpaEntity, Long>, NewsRepositoryCustom,
    QuerydslPredicateExecutor<NewsJpaEntity> {

}
