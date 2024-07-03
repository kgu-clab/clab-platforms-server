package page.clab.api.domain.activityPhoto.application.port.in;

import page.clab.api.domain.activityPhoto.dto.request.ActivityPhotoRequestDto;

public interface RegisterActivityPhotoUseCase {
    Long register(ActivityPhotoRequestDto requestDto);
}
