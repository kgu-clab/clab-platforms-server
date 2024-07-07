package page.clab.api.domain.activityPhoto.application.port.out;

import page.clab.api.domain.activityPhoto.domain.ActivityPhoto;

public interface RemoveActivityPhotoPort {
    void delete(ActivityPhoto activityPhoto);
}
