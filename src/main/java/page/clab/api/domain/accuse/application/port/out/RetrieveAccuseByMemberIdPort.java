package page.clab.api.domain.accuse.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.accuse.domain.Accuse;

import java.util.List;

public interface RetrieveAccuseByMemberIdPort {
    Page<Accuse> findByMemberId(String memberId, Pageable pageable);
    List<Accuse> findByMemberId(String memberId);
}
