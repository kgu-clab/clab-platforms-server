package page.clab.api.domain.application.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.application.domain.Application;
import page.clab.api.domain.application.domain.ApplicationId;

import java.util.Optional;

public interface RetrieveApplicationPort {
    Optional<Application> findById(ApplicationId applicationId);
    Application findByIdOrThrow(ApplicationId applicationId);
    Page<Application> findByConditions(Long recruitmentId, String studentId, Boolean isPass, Pageable pageable);
    Page<Application> findAllByIsDeletedTrue(Pageable pageable);
}
