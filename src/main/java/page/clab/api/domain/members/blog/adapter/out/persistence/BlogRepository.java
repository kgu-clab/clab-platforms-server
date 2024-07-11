package page.clab.api.domain.members.blog.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<BlogJpaEntity, Long>, BlogRepositoryCustom, QuerydslPredicateExecutor<BlogJpaEntity> {

    @Query(value = "SELECT b.* FROM blog b WHERE b.is_deleted = true", nativeQuery = true)
    Page<BlogJpaEntity> findAllByIsDeletedTrue(Pageable pageable);

    List<BlogJpaEntity> findByMemberId(String memberId);
}
