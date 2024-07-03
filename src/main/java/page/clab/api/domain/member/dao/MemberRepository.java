package page.clab.api.domain.member.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.member.domain.Member;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, String>, MemberRepositoryCustom, QuerydslPredicateExecutor<Member> {
    boolean existsByContact(String contact);
    boolean existsByEmail(String email);
    Optional<Member> findByEmail(String email);
    Page<Member> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
