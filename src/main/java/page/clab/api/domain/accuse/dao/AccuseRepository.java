package page.clab.api.domain.accuse.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.accuse.domain.Accuse;
import page.clab.api.domain.accuse.domain.AccuseTarget;
import page.clab.api.domain.member.domain.Member;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccuseRepository extends JpaRepository<Accuse, Long> {

    Page<Accuse> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Optional<Accuse> findByMemberAndTarget(Member member, AccuseTarget target);

    List<Accuse> findByTargetOrderByCreatedAtDesc(AccuseTarget accuseTarget);

    Page<Accuse> findByMemberOrderByCreatedAtDesc(Member currentMember, Pageable pageable);

    List<Accuse> findByTarget(AccuseTarget target);

}
