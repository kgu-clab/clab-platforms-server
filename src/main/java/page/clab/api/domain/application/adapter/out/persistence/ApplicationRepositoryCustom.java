package page.clab.api.domain.application.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.application.domain.Application;

public interface ApplicationRepositoryCustom {
    Page<Application> findByConditions(Long recruitmentId, String studentId, Boolean isPass, Pageable pageable);
}
