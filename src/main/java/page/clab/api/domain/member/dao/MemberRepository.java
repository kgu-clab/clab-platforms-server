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

    Optional<Member> findByContact(String contact);

    Optional<Object> findByEmail(String email);

    Page<Member> findAllByOrderByCreatedAtDesc(Pageable pageable);

}
