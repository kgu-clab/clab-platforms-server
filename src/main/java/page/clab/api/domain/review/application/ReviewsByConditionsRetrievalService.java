package page.clab.api.domain.review.application;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.review.dto.response.ReviewResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface ReviewsByConditionsRetrievalService {
    PagedResponseDto<ReviewResponseDto> retrieveByConditions(String memberId, String memberName, Long activityId, Boolean isPublic, Pageable pageable);
}
