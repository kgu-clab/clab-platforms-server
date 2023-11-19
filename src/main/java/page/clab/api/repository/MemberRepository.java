package page.clab.api.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import page.clab.api.type.entity.Member;
import page.clab.api.type.etc.MemberStatus;

public interface MemberRepository extends JpaRepository<Member, String> {

    Page<Member> findAllByName(String name, Pageable pageable);

    Page<Member> findByMemberStatus(MemberStatus memberStatus, Pageable pageable);

    Optional<Member> findByContact(String contact);

}
