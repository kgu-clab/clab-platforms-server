package page.clab.api.domain.community.accuse.adapter.out.persistence;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.community.accuse.domain.TargetType;

@Repository
public interface AccuseRepository extends JpaRepository<AccuseJpaEntity, Long> {

    @Query("SELECT a FROM AccuseJpaEntity a WHERE a.isDeleted = false AND a.memberId = :memberId AND a.target.targetType = :targetType AND a.target.targetReferenceId = :targetReferenceId")
    Optional<AccuseJpaEntity> findByMemberIdAndTarget(String memberId, TargetType targetType, Long targetReferenceId);

    @Query("SELECT a FROM AccuseJpaEntity a WHERE a.isDeleted = false AND a.target.targetType = :targetType AND a.target.targetReferenceId = :targetReferenceId ORDER BY a.createdAt DESC")
    List<AccuseJpaEntity> findByTargetOrderByCreatedAtDesc(TargetType targetType, Long targetReferenceId);

    @Query("SELECT a FROM AccuseJpaEntity a WHERE a.isDeleted = false AND a.memberId = :memberId ORDER BY a.createdAt DESC")
    Page<AccuseJpaEntity> findByMemberId(String memberId, Pageable pageable);

    @Query("SELECT a FROM AccuseJpaEntity a WHERE a.isDeleted = false AND a.memberId = :memberId")
    List<AccuseJpaEntity> findByMemberId(String memberId);

    @Query("SELECT a FROM AccuseJpaEntity a WHERE a.isDeleted = false AND a.target.targetType = :targetType AND a.target.targetReferenceId = :targetReferenceId")
    List<AccuseJpaEntity> findByTarget(TargetType targetType, Long targetReferenceId);
}
