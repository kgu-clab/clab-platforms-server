package page.clab.api.domain.activityPhoto.application.port.in;

import page.clab.api.domain.activityPhoto.application.dto.request.ActivityPhotoRequestDto;

public interface RegisterActivityPhotoUseCase {
    Long registerActivityPhoto(ActivityPhotoRequestDto requestDto);
}
