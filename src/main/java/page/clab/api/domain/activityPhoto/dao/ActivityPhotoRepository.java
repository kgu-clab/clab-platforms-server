package page.clab.api.domain.activityPhoto.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.activityPhoto.domain.ActivityPhoto;

@Repository
public interface ActivityPhotoRepository extends JpaRepository<ActivityPhoto, Long> {

    Page<ActivityPhoto> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<ActivityPhoto> findAllByIsPublicTrueOrderByCreatedAtDesc(Pageable pageable);

}
