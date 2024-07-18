package page.clab.api.domain.memberManagement.member.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberRepositoryCustom {

    Page<MemberJpaEntity> findByConditions(String id, String name, Pageable pageable);

    Page<MemberJpaEntity> findBirthdaysThisMonth(int month, Pageable pageable);
}
