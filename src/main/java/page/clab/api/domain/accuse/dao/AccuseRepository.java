package page.clab.api.domain.accuse.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.accuse.domain.Accuse;
import page.clab.api.domain.accuse.domain.TargetType;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccuseRepository extends JpaRepository<Accuse, Long> {

    @Query("SELECT a FROM Accuse a WHERE a.isDeleted = false AND a.memberId = :memberId AND a.target.targetType = :targetType AND a.target.targetReferenceId = :targetReferenceId")
    Optional<Accuse> findByMemberIdAndTarget(String memberId, TargetType targetType, Long targetReferenceId);

    @Query("SELECT a FROM Accuse a WHERE a.isDeleted = false AND a.target.targetType = :targetType AND a.target.targetReferenceId = :targetReferenceId ORDER BY a.createdAt DESC")
    List<Accuse> findByTargetOrderByCreatedAtDesc(TargetType targetType, Long targetReferenceId);

    @Query("SELECT a FROM Accuse a WHERE a.isDeleted = false AND a.memberId = :memberId ORDER BY a.createdAt DESC")
    Page<Accuse> findByMemberId(String memberId, Pageable pageable);

    @Query("SELECT a FROM Accuse a WHERE a.isDeleted = false AND a.memberId = :memberId")
    List<Accuse> findByMemberId(String memberId);

    @Query("SELECT a FROM Accuse a WHERE a.isDeleted = false AND a.target.targetType = :targetType AND a.target.targetReferenceId = :targetReferenceId")
    List<Accuse> findByTarget(TargetType targetType, Long targetReferenceId);

}
