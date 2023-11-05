package page.clab.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import page.clab.api.type.entity.Application;

import java.time.LocalDateTime;
import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, String> {

    @Query("SELECT a FROM Application a WHERE a.updateTime >= :startDate AND a.updateTime <= :endDate")
    List<Application> findApplicationsBetweenDates(LocalDateTime startDate, LocalDateTime endDate);

    List<Application> findAllByIsPass(boolean isPass);

}
