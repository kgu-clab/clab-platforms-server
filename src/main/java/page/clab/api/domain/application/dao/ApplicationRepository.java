package page.clab.api.domain.application.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import page.clab.api.domain.application.domain.Application;

public interface ApplicationRepository extends JpaRepository<Application, String> {

    Page<Application> findAllByOrderByCreatedAtDesc(Pageable pageable);

}
