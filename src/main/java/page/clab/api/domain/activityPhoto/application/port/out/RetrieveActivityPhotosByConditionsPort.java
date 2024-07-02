package page.clab.api.domain.activityPhoto.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.activityPhoto.domain.ActivityPhoto;

public interface RetrieveActivityPhotosByConditionsPort {
    Page<ActivityPhoto> findByConditions(Boolean isPublic, Pageable pageable);
}
