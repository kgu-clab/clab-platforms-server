package page.clab.api.domain.award.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.award.domain.Award;
import page.clab.api.domain.member.domain.Member;

@Repository
public interface AwardRepository extends JpaRepository<Award, Long>, AwardRepositoryCustom, QuerydslPredicateExecutor<Award> {

    Page<Award> findAllByMember(Member member, Pageable pageable);

    @Query(value = "SELECT a.* FROM award a WHERE a.is_deleted = true", nativeQuery = true)
    Page<Award> findAllByIsDeletedTrue(Pageable pageable);

}
