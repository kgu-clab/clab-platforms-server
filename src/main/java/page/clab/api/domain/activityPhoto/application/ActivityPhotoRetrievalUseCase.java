package page.clab.api.domain.activityPhoto.application;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.activityPhoto.dto.response.ActivityPhotoResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface ActivityPhotoRetrievalUseCase {
    PagedResponseDto<ActivityPhotoResponseDto> retrieve(Boolean isPublic, Pageable pageable);
}
