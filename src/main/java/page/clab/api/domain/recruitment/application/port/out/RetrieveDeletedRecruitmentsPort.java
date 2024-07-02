package page.clab.api.domain.recruitment.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.recruitment.domain.Recruitment;

public interface RetrieveDeletedRecruitmentsPort {
    Page<Recruitment> findAllByIsDeletedTrue(Pageable pageable);
}
