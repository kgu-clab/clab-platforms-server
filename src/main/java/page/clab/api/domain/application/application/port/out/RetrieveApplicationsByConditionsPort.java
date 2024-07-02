package page.clab.api.domain.application.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.application.domain.Application;

public interface RetrieveApplicationsByConditionsPort {
    Page<Application> findByConditions(Long recruitmentId, String studentId, Boolean isPass, Pageable pageable);
}
