package page.clab.api.domain.activityPhoto.application.port.out;

import page.clab.api.domain.activityPhoto.domain.ActivityPhoto;

import java.util.Optional;

public interface LoadActivityPhotoPort {
    Optional<ActivityPhoto> findById(Long activityPhotoId);
    ActivityPhoto findByIdOrThrow(Long activityPhotoId);
}
