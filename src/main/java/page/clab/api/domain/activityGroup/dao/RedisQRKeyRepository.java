package page.clab.api.domain.activityGroup.dao;

import org.springframework.data.repository.CrudRepository;
import page.clab.api.domain.activityGroup.domain.RedisQRKey;

public interface RedisQRKeyRepository extends CrudRepository<RedisQRKey, String> {
    boolean existsByQRCodeKey(String qrCodeData);
}
