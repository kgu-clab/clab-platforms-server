package page.clab.api.domain.members.activityPhoto.application.port.in;

import page.clab.api.domain.members.activityPhoto.application.dto.request.ActivityPhotoRequestDto;

public interface RegisterActivityPhotoUseCase {
    Long registerActivityPhoto(ActivityPhotoRequestDto requestDto);
}
