package page.clab.api.domain.application.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.application.domain.Application;

public interface RetrieveDeletedApplicationsPort {
    Page<Application> findAllByIsDeletedTrue(Pageable pageable);
}
