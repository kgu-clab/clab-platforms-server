package page.clab.api.domain.position.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.position.domain.Position;
import page.clab.api.domain.position.domain.PositionType;

import java.util.List;
import java.util.Optional;

public interface PositionRepository extends JpaRepository<Position, Long>, PositionRepositoryCustom, QuerydslPredicateExecutor<Position> {

    Optional<Position> findByMemberAndYearAndPositionType(Member member, String year, PositionType positionType);

    List<Position> findAllByMemberAndYearOrderByPositionTypeAsc(Member member, String year);

    @Query(value = "SELECT p.* FROM \"position\" p WHERE p.is_deleted = true", nativeQuery = true)
    Page<Position> findAllByIsDeletedTrue(Pageable pageable);

}
