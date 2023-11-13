package page.clab.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.type.entity.Accuse;

@Repository
public interface AccuseRepository extends JpaRepository<Accuse, Long> {

}
