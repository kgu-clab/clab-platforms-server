package page.clab.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.type.entity.Accuse;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccuseRepository extends JpaRepository<Accuse, Long> {

    Optional<Accuse> findByTargetId(Long targetId);

    List<Accuse> findAllByTargetId(Long targetId);

    List<Accuse> findAllByTargetIdAndCategory(Long targetId, String category);
    
}
