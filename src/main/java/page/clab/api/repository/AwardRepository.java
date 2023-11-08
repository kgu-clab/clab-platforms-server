package page.clab.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.type.entity.Award;
import page.clab.api.type.entity.Member;

import java.util.List;

@Repository
public interface AwardRepository extends JpaRepository<Award, Long> {

    List<Award> findAllByMember(Member member);

}
