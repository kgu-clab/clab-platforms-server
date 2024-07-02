package page.clab.api.domain.activityPhoto.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.activityPhoto.domain.ActivityPhoto;

@Repository
public interface ActivityPhotoRepository extends JpaRepository<ActivityPhoto, Long>, ActivityPhotoRepositoryCustom, QuerydslPredicateExecutor<ActivityPhoto> {
}
