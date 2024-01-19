package page.clab.api.domain.accuse.dao;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.accuse.domain.Accuse;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.accuse.domain.AccuseStatus;
import page.clab.api.domain.accuse.domain.TargetType;

@Repository
public interface AccuseRepository extends JpaRepository<Accuse, Long> {

    Page<Accuse> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<Accuse> findAllByTargetTypeAndAccuseStatusOrderByCreatedAtDesc(TargetType targetType, AccuseStatus accuseStatus, Pageable pageable);

    Page<Accuse> findAllByTargetTypeOrderByCreatedAtDesc(TargetType targetType, Pageable pageable);

    Page<Accuse> findAllByAccuseStatusOrderByCreatedAtDesc(AccuseStatus accuseStatus, Pageable pageable);

    Optional<Accuse> findByMemberAndTargetTypeAndTargetId(Member member, TargetType targetType, Long targetId);

}
