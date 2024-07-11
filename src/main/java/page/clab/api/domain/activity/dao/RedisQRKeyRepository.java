package page.clab.api.domain.activity.dao;

import org.springframework.data.repository.CrudRepository;
import page.clab.api.domain.activity.domain.RedisQRKey;

public interface RedisQRKeyRepository extends CrudRepository<RedisQRKey, String> {
    boolean existsByQRCodeKey(String qrCodeData);
}
