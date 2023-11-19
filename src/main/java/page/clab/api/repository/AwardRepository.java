package page.clab.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.type.entity.Award;
import page.clab.api.type.entity.Member;

@Repository
public interface AwardRepository extends JpaRepository<Award, Long> {

    Page<Award> findAllByMember(Member member, Pageable pageable);

}
