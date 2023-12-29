package page.clab.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import page.clab.api.type.entity.Application;

import java.time.LocalDateTime;

public interface ApplicationRepository extends JpaRepository<Application, String> {

    Page<Application> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<Application> findAllByUpdateTimeBetweenOrderByCreatedAtDesc(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    Page<Application> findAllByIsPassOrderByCreatedAtDesc(boolean isPass, Pageable pageable);

}
