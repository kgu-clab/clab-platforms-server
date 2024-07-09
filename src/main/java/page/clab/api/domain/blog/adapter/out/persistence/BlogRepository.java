package page.clab.api.domain.blog.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.blog.domain.Blog;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long>, BlogRepositoryCustom, QuerydslPredicateExecutor<Blog> {

    Page<Blog> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Query(value = "SELECT b.* FROM blog b WHERE b.is_deleted = true", nativeQuery = true)
    Page<Blog> findAllByIsDeletedTrue(Pageable pageable);

    List<Blog> findByMemberId(String memberId);
}
