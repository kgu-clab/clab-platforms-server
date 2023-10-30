package page.clab.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import page.clab.api.type.entity.SharedAccount;

public interface SharedAccountRepository extends JpaRepository<SharedAccount, Long> {

}
