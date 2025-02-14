package page.clab.api.domain.memberManagement.award.adapter.out.persistence;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AwardRepository extends JpaRepository<AwardJpaEntity, Long>, AwardRepositoryCustom {

    Page<AwardJpaEntity> findByMemberId(String memberId, Pageable pageable);

    @Query(value = "SELECT a.* FROM award a WHERE a.is_deleted = true", nativeQuery = true)
    Page<AwardJpaEntity> findAllByIsDeletedTrue(Pageable pageable);

    List<AwardJpaEntity> findByMemberId(String memberId);
}
