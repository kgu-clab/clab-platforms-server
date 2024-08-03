package page.clab.api.domain.hiring.application.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ApplicationRepositoryCustom {
    Page<ApplicationJpaEntity> findByConditions(Long recruitmentId, String studentId, Boolean isPass, Pageable pageable);
}
