package page.clab.api.domain.activityPhoto.application;

import page.clab.api.domain.activityPhoto.dto.request.ActivityPhotoRequestDto;

public interface CreateActivityPhotoService {
    Long execute(ActivityPhotoRequestDto requestDto);
}
