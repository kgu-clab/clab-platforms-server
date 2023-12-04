package page.clab.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.type.entity.ActivityPhoto;

@Repository
public interface ActivityPhotoRepository extends JpaRepository<ActivityPhoto, Long> {

    Page<ActivityPhoto> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<ActivityPhoto> findAllByIsPublicTrueOrderByCreatedAtDesc(Pageable pageable);

}
