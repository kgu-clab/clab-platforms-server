package page.clab.api.repository;

import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import page.clab.api.type.entity.Application;

public interface ApplicationRepository extends JpaRepository<Application, String> {

    Page<Application> findAllByUpdateTimeBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    Page<Application> findAllByIsPass(boolean isPass, Pageable pageable);

}
