package page.clab.api.domain.member.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.member.domain.Member;

import java.util.List;
import java.util.Optional;

public interface RetrieveMemberPort {
    Optional<Member> findById(String memberId);
    Member findByIdOrThrow(String memberId);
    List<Member> findAll();
    Page<Member> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Member findByEmailOrThrow(String email);
    Page<Member> findBirthdaysThisMonth(int month, Pageable pageable);
    Page<Member> findByConditions(String id, String name, Pageable pageable);
}
