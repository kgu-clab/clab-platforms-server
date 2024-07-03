package page.clab.api.domain.member.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.member.domain.Member;

public interface RetrieveMembersByBirthdayPort {
    Page<Member> findBirthdaysThisMonth(int month, Pageable pageable);
}
