package page.clab.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.type.entity.Member;
import page.clab.api.type.etc.MemberStatus;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

    Optional<Member> findByContact(String contact);

    Optional<Object> findByEmail(String email);

    Page<Member> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<Member> findAllByNameOrderByCreatedAtDesc(String name, Pageable pageable);

    Page<Member> findByMemberStatusOrderByCreatedAtDesc(MemberStatus memberStatus, Pageable pageable);

}
