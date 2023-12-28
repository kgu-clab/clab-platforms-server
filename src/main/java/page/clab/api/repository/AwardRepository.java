package page.clab.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.type.entity.Award;
import page.clab.api.type.entity.Member;

import java.time.LocalDate;

@Repository
public interface AwardRepository extends JpaRepository<Award, Long> {
    Page<Award> findAllByMemberOrderByAwardDateDesc(Member member, Pageable pageable);

    Page<Award> findAllByAwardDateBetween(LocalDate startOfYear, LocalDate endOfYear, Pageable pageable);

}
