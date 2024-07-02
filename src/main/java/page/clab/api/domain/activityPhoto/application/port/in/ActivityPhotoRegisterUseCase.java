package page.clab.api.domain.activityPhoto.application.port.in;

import page.clab.api.domain.activityPhoto.dto.request.ActivityPhotoRequestDto;

public interface ActivityPhotoRegisterUseCase {
    Long register(ActivityPhotoRequestDto requestDto);
}
