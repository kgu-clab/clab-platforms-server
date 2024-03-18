package page.clab.api.domain.accuse.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.accuse.domain.Accuse;
import page.clab.api.domain.accuse.domain.TargetType;
import page.clab.api.domain.member.domain.Member;

import java.util.Optional;

@Repository
public interface AccuseRepository extends JpaRepository<Accuse, Long>, AccuseRepositoryCustom, QuerydslPredicateExecutor<Accuse> {

    Page<Accuse> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Optional<Accuse> findByMemberAndTargetTypeAndTargetId(Member member, TargetType targetType, Long targetId);

}
