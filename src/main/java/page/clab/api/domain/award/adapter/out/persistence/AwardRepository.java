package page.clab.api.domain.award.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.award.domain.Award;

import java.util.List;

@Repository
public interface AwardRepository extends JpaRepository<Award, Long>, AwardRepositoryCustom, QuerydslPredicateExecutor<Award> {

    Page<Award> findByMemberId(String memberId, Pageable pageable);

    @Query(value = "SELECT a.* FROM award a WHERE a.is_deleted = true", nativeQuery = true)
    Page<Award> findAllByIsDeletedTrue(Pageable pageable);

    List<Award> findByMemberId(String memberId);
}
