package page.clab.api.domain.members.support.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.members.support.domain.Support;

public interface RetrieveSupportPort {

    Support getById(Long id);

    Page<Support> findAll(Pageable pageable);

    Page<Support> findAllByWriterId(String memberId, Pageable pageable);

    Page<Support> findAllAccessible(String memberId, Pageable pageable);
}