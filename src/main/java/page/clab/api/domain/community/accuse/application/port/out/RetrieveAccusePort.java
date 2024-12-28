package page.clab.api.domain.community.accuse.application.port.out;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.community.accuse.domain.Accuse;
import page.clab.api.domain.community.accuse.domain.TargetType;

public interface RetrieveAccusePort {

    Optional<Accuse> findByMemberIdAndTarget(String memberId, TargetType targetType, Long targetReferenceId);

    List<Accuse> findByTargetOrderByCreatedAtDesc(TargetType targetType, Long targetReferenceId);

    List<Accuse> findByTarget(TargetType targetType, Long targetReferenceId);

    Page<Accuse> findByMemberId(String memberId, Pageable pageable);

    List<Accuse> findByMemberId(String memberId);
}
