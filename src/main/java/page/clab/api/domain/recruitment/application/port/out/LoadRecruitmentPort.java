package page.clab.api.domain.recruitment.application.port.out;

import page.clab.api.domain.recruitment.domain.Recruitment;

import java.util.List;
import java.util.Optional;

public interface LoadRecruitmentPort {
    Optional<Recruitment> findById(Long recruitmentId);
    Recruitment findByIdOrThrow(Long recruitmentId);
    List<Recruitment> findAll();
}
