package page.clab.api.domain.position.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import page.clab.api.domain.position.domain.Position;
import page.clab.api.domain.position.domain.PositionType;

import java.util.List;
import java.util.Optional;

public interface PositionRepository extends JpaRepository<Position, Long>, PositionRepositoryCustom, QuerydslPredicateExecutor<Position> {

    List<Position> findByMemberId(String id);

    Optional<Position> findByMemberIdAndYearAndPositionType(String memberId, String year, PositionType positionType);

    List<Position> findAllByMemberIdAndYearOrderByPositionTypeAsc(String memberId, String year);

    @Query(value = "SELECT p.* FROM \"position\" p WHERE p.is_deleted = true", nativeQuery = true)
    Page<Position> findAllByIsDeletedTrue(Pageable pageable);

}
