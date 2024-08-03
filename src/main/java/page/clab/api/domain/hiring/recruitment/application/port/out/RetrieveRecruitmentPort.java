package page.clab.api.domain.hiring.recruitment.application.port.out;

import page.clab.api.domain.hiring.recruitment.domain.Recruitment;

import java.util.List;

public interface RetrieveRecruitmentPort {

    Recruitment findByIdOrThrow(Long recruitmentId);

    List<Recruitment> findAll();

    List<Recruitment> findTop5ByOrderByCreatedAtDesc();

    void existsByIdOrThrow(Long recruitmentId);
}
