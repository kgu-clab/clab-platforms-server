package page.clab.api.domain.members.support.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SupportAnswerRepository extends JpaRepository<SupportAnswerJpaEntity, Long> {
    SupportAnswerJpaEntity findBySupportId(Long supportId);
}
