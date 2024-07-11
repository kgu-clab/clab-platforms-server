package page.clab.api.domain.activityPhoto.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityPhotoRepository extends JpaRepository<ActivityPhotoJpaEntity, Long>, ActivityPhotoRepositoryCustom, QuerydslPredicateExecutor<ActivityPhotoJpaEntity> {
}
