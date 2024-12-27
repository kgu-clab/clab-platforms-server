package page.clab.api.domain.memberManagement.executive.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExecutiveRepository extends JpaRepository<ExecutiveJpaEntity, String> {
}
