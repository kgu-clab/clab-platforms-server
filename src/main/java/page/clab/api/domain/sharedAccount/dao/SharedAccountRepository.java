package page.clab.api.domain.sharedAccount.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import page.clab.api.domain.sharedAccount.domain.SharedAccount;

public interface SharedAccountRepository extends JpaRepository<SharedAccount, Long> {

    Page<SharedAccount> findAllByOrderByIdAsc(Pageable pageable);

}
