package page.clab.api.global.common.verification.dao;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.global.common.verification.domain.Verification;

@Repository
public interface VerificationRepository extends CrudRepository<Verification, String> {

    Optional<Verification> findByVerificationCode(String verificationCode);
}
