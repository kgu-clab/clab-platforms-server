package page.clab.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import page.clab.api.type.entity.Application;

public interface ApplicationRepository extends JpaRepository<Application, String> {

}
