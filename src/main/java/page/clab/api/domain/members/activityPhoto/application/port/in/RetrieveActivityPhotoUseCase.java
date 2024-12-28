package page.clab.api.domain.members.activityPhoto.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.members.activityPhoto.application.dto.response.ActivityPhotoResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface RetrieveActivityPhotoUseCase {

    PagedResponseDto<ActivityPhotoResponseDto> retrieveActivityPhotos(Boolean isPublic, Pageable pageable);
}
