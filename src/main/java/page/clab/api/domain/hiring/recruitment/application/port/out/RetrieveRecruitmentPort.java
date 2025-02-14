package page.clab.api.domain.hiring.recruitment.application.port.out;

import java.time.LocalDateTime;
import java.util.List;
import page.clab.api.domain.hiring.recruitment.domain.Recruitment;
import page.clab.api.domain.hiring.recruitment.domain.RecruitmentStatus;

public interface RetrieveRecruitmentPort {

    Recruitment getById(Long recruitmentId);

    List<Recruitment> findAll();

    List<Recruitment> findTop5ByOrderByCreatedAtDesc();

    List<Recruitment> findByEndDateBetween(LocalDateTime weekAgo, LocalDateTime now);

    List<Recruitment> findByStatus(RecruitmentStatus status);
}
