package page.clab.api.domain.member.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.member.domain.Member;

public interface RetrieveMembersByConditionsPort {
    Page<Member> findByConditions(String id, String name, Pageable pageable);
}
