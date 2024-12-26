package page.clab.api.domain.hiring.recruitment.application.port.out;

import java.time.LocalDateTime;
import java.util.List;
import page.clab.api.domain.hiring.recruitment.domain.Recruitment;

public interface RetrieveRecruitmentPort {

    Recruitment getById(Long recruitmentId);

    List<Recruitment> findAll();

    List<Recruitment> findTop5ByOrderByCreatedAtDesc();

    List<Recruitment> findByEndDateBetween(LocalDateTime weekAgo, LocalDateTime now);
}
