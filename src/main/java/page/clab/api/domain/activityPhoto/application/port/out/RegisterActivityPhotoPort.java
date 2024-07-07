package page.clab.api.domain.activityPhoto.application.port.out;

import page.clab.api.domain.activityPhoto.domain.ActivityPhoto;

public interface RegisterActivityPhotoPort {
    ActivityPhoto save(ActivityPhoto activityPhoto);
}
