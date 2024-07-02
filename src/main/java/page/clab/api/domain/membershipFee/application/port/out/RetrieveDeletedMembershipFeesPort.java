package page.clab.api.domain.membershipFee.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.membershipFee.domain.MembershipFee;

public interface RetrieveDeletedMembershipFeesPort {
    Page<MembershipFee> findAllByIsDeletedTrue(Pageable pageable);
}
