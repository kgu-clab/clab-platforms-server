package page.clab.api.domain.login.dao;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.login.domain.RedisQRKey;

@Repository
public interface RedisQRKeyRepository extends CrudRepository<RedisQRKey, String> {

    Optional<RedisQRKey> findByQRCodeKey(String QRCodeKey);
}
