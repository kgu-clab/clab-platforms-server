package page.clab.api.domain.position.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.position.domain.Position;
import page.clab.api.domain.position.domain.PositionType;

public interface PositionRepository extends JpaRepository<Position, Long> {

    Optional<Position> findByMemberAndYearAndPositionType(Member member, String year, PositionType positionType);

    Page<Position> findAllByOrderByYearDescPositionTypeAsc(Pageable pageable);

    Page<Position> findAllByYearOrderByPositionTypeAsc(String year, Pageable pageable);

    Page<Position> findAllByYearAndPositionTypeOrderByPositionTypeAsc(String year, PositionType positionType, Pageable pageable);

    Page<Position> findAllByPositionTypeOrderByYearDescPositionTypeAsc(PositionType positionType, Pageable pageable);

    List<Position> findAllByMemberAndYearOrderByPositionTypeAsc(Member member, String year);

}
