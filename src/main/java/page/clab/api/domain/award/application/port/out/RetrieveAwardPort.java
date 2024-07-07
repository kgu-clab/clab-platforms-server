package page.clab.api.domain.award.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.award.domain.Award;

import java.util.Optional;

public interface RetrieveAwardPort {
    Optional<Award> findById(Long awardId);

    Award findByIdOrThrow(Long awardId);

    Page<Award> findByConditions(String memberId, Long year, Pageable pageable);

    Page<Award> findAllByIsDeletedTrue(Pageable pageable);

    Page<Award> findByMemberId(String memberId, Pageable pageable);
}
