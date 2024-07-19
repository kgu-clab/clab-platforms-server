package page.clab.api.domain.members.activityPhoto.application.port.out;

import page.clab.api.domain.members.activityPhoto.domain.ActivityPhoto;

public interface RegisterActivityPhotoPort {
    ActivityPhoto save(ActivityPhoto activityPhoto);
}
