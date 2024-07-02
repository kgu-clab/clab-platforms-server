package page.clab.api.domain.award.application.port.out;

import page.clab.api.domain.award.domain.Award;

import java.util.Optional;

public interface RetrieveAwardByIdPort {
    Optional<Award> findById(Long awardId);
    Award findByIdOrThrow(Long awardId);
}
