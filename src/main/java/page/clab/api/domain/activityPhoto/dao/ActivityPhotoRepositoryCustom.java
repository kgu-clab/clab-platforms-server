package page.clab.api.domain.activityPhoto.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.activityPhoto.domain.ActivityPhoto;

public interface ActivityPhotoRepositoryCustom {
    Page<ActivityPhoto> findByConditions(Boolean isPublic, Pageable pageable);
}
