package page.clab.api.domain.memberManagement.member.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberJpaEntity, String>, MemberRepositoryCustom, QuerydslPredicateExecutor<MemberJpaEntity> {
    boolean existsByContact(String contact);

    boolean existsByEmail(String email);

    Optional<MemberJpaEntity> findByEmail(String email);

    Page<MemberJpaEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
