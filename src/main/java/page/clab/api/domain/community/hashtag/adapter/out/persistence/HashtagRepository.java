package page.clab.api.domain.community.hashtag.adapter.out.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashtagRepository extends JpaRepository<HashtagJpaEntity, Long> {

    boolean existsByName(String name);

    HashtagJpaEntity findByName(String name);

    List<HashtagJpaEntity> findAllByOrderById();
}
