package page.clab.api.domain.members.support.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupportRepository extends JpaRepository<SupportJpaEntity, Long>{

    @Query("SELECT s FROM SupportJpaEntity s WHERE s.memberId = ?1 AND s.isDeleted = false")
    Page<SupportJpaEntity> findAllByMemberIdAndIsDeletedFalse(String memberId, Pageable pageable);

    List<SupportJpaEntity> findAllByMemberId(String memberId);

    @Query("SELECT s FROM SupportJpaEntity s WHERE s.isDeleted = false AND (s.category != 'BUG' OR s.memberId = :memberId)")
    Page<SupportJpaEntity> findAllAccessible(String memberId, Pageable pageable);
}
