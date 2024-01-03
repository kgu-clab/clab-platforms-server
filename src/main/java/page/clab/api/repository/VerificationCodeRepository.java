package page.clab.api.repository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.type.entity.VerificationCode;

@Repository
public interface VerificationCodeRepository extends CrudRepository<VerificationCode, String> {
    
    Optional<VerificationCode> findByVerificationCode(String verificationCode);

}
