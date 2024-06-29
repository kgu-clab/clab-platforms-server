package page.clab.api.domain.activityPhoto.application;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.activityPhoto.dto.response.ActivityPhotoResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface ActivityPhotoRetrievalService {
    PagedResponseDto<ActivityPhotoResponseDto> retrievePhotos(Boolean isPublic, Pageable pageable);
}
