package page.clab.api.domain.member.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.member.domain.Role;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

    Optional<Member> findByContact(String contact);

    Optional<Object> findByEmail(String email);

    List<Member> findAllByRole(Role role);

    Page<Member> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<Member> findAllByNameOrderByCreatedAtDesc(String name, Pageable pageable);

}
