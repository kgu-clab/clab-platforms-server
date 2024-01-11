package page.clab.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.type.entity.RedisQRKey;

@Repository
public interface RedisQRKeyRepository extends CrudRepository<RedisQRKey, String> {

}
