package page.clab.api.domain.application.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import page.clab.api.domain.application.domain.Application;
import page.clab.api.domain.application.domain.ApplicationId;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, ApplicationId> {

    Page<Application> findAllByOrderByCreatedAtDesc(Pageable pageable);

    List<Application> findByRecruitmentIdAndIsPass(Long recruitmentId, Boolean isPass);

    Optional<Application> findByRecruitmentIdAndStudentId(Long recruitmentId, String studentId);

}
