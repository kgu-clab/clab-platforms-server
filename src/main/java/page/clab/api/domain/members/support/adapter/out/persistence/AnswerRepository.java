package page.clab.api.domain.members.support.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<AnswerJpaEntity, Long> {

    AnswerJpaEntity findBySupportId(Long supportId);
}
