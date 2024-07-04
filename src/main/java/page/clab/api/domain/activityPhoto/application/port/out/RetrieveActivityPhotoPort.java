package page.clab.api.domain.activityPhoto.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.activityPhoto.domain.ActivityPhoto;

import java.util.Optional;

public interface RetrieveActivityPhotoPort {
    Optional<ActivityPhoto> findById(Long activityPhotoId);

    ActivityPhoto findByIdOrThrow(Long activityPhotoId);

    Page<ActivityPhoto> findByConditions(Boolean isPublic, Pageable pageable);
}
