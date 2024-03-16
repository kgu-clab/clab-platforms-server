package page.clab.api.domain.member.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.member.domain.Member;

public interface MemberRepositoryCustom {

    Page<Member> findByConditions(String id, String name, Pageable pageable);

}
