package page.clab.api.domain.member.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.member.domain.Member;

public interface MemberRepositoryCustom {
    Page<Member> findByConditions(String id, String name, Pageable pageable);

    Page<Member> findBirthdaysThisMonth(int month, Pageable pageable);
}
