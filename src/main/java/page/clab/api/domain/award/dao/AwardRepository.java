package page.clab.api.domain.award.dao;

import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.award.domain.Award;
import page.clab.api.domain.member.domain.Member;

@Repository
public interface AwardRepository extends JpaRepository<Award, Long> {
    Page<Award> findAllByMemberOrderByAwardDateDesc(Member member, Pageable pageable);

    Page<Award> findAllByAwardDateBetween(LocalDate startOfYear, LocalDate endOfYear, Pageable pageable);

}
