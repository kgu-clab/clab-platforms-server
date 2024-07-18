package page.clab.api.domain.hiring.recruitment.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.hiring.recruitment.domain.Recruitment;

import java.util.List;

public interface RetrieveRecruitmentPort {

    Recruitment findByIdOrThrow(Long recruitmentId);

    List<Recruitment> findAll();

    Page<Recruitment> findAllByIsDeletedTrue(Pageable pageable);

    List<Recruitment> findTop5ByOrderByCreatedAtDesc();

    void existsByIdOrThrow(Long recruitmentId);
}
