package page.clab.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import page.clab.api.type.entity.Member;
import page.clab.api.type.etc.MemberStatus;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {

    Optional<Member> findByName(String name);

    Optional<Member> findByContact(String contact);

    Optional<Member> findById(String id);

    List<Member> findByMemberStatus(MemberStatus memberStatus);

}
