package page.clab.api.domain.hiring.recruitment.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecruitmentRepository extends JpaRepository<RecruitmentJpaEntity, Long> {

    List<RecruitmentJpaEntity> findTop5ByOrderByCreatedAtDesc();

    @Query(value = "SELECT r.* FROM recruitment r WHERE r.is_deleted = true", nativeQuery = true)
    Page<RecruitmentJpaEntity> findAllByIsDeletedTrue(Pageable pageable);
}
