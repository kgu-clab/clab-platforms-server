package page.clab.api.global.common.softDelete;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SoftDeleteRepository extends JpaRepository<SoftDeletedEntity, Long> {

}
