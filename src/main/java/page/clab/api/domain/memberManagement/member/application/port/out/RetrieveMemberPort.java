package page.clab.api.domain.memberManagement.member.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.memberManagement.member.domain.Member;
import page.clab.api.domain.memberManagement.member.domain.Role;

import java.util.List;
import java.util.Optional;

public interface RetrieveMemberPort {

    Optional<Member> findById(String memberId);

    Member findByIdOrThrow(String memberId);

    List<Member> findAll();

    List<Member> findAll(Pageable pageable);

    Page<Member> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Member findByEmailOrThrow(String email);

    Page<Member> findBirthdaysThisMonth(int month, Pageable pageable);

    Page<Member> findByConditions(String id, String name, Pageable pageable);

    Member findFirstByRoleOrThrow(Role role);
}
