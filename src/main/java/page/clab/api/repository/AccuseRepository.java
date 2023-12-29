package page.clab.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.type.entity.Accuse;
import page.clab.api.type.entity.Member;
import page.clab.api.type.etc.AccuseStatus;
import page.clab.api.type.etc.TargetType;

import java.util.Optional;

@Repository
public interface AccuseRepository extends JpaRepository<Accuse, Long> {

    Page<Accuse> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<Accuse> findAllByTargetTypeAndAccuseStatusOrderByCreatedAtDesc(TargetType targetType, AccuseStatus accuseStatus, Pageable pageable);

    Page<Accuse> findAllByTargetTypeOrderByCreatedAtDesc(TargetType targetType, Pageable pageable);

    Page<Accuse> findAllByAccuseStatusOrderByCreatedAtDesc(AccuseStatus accuseStatus, Pageable pageable);

    Optional<Accuse> findByMemberAndTargetTypeAndTargetId(Member member, TargetType targetType, Long targetId);

}
