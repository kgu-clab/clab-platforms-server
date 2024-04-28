package page.clab.api.domain.blog.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.blog.domain.Blog;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long>, BlogRepositoryCustom, QuerydslPredicateExecutor<Blog> {

    Page<Blog> findAllByOrderByCreatedAtDesc(Pageable pageable);

}
