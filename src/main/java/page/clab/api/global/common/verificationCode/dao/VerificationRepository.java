package page.clab.api.global.common.verificationCode.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.global.common.verificationCode.domain.Verification;

import java.util.Optional;

@Repository
public interface VerificationRepository extends CrudRepository<Verification, String> {
    
    Optional<Verification> findByVerificationCode(String verificationCode);

}
