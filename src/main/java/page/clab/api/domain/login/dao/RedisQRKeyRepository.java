package page.clab.api.domain.login.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.login.domain.RedisQRKey;

@Repository
public interface RedisQRKeyRepository extends CrudRepository<RedisQRKey, String> {
    boolean existsByQRCodeKey(String qrCodeData);
}
