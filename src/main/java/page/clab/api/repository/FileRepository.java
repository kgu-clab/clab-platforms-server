package page.clab.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import page.clab.api.type.entity.File;

public interface FileRepository extends JpaRepository<File, Long> {
}
