package page.clab.api.domain.news.dao;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.news.domain.News;

@Repository
public interface NewsRepository extends JpaRepository<News, Long>, NewsRepositoryCustom, QuerydslPredicateExecutor<News> {

    Page<News> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Query(value = "SELECT n.* FROM news n WHERE n.is_deleted = true", nativeQuery = true)
    Page<News> findAllByIsDeletedTrue(Pageable pageable);

}
