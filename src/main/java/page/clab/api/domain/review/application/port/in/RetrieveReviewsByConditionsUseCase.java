package page.clab.api.domain.review.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.review.dto.response.ReviewResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface RetrieveReviewsByConditionsUseCase {
    PagedResponseDto<ReviewResponseDto> retrieve(String memberId, String memberName, Long activityId, Boolean isPublic, Pageable pageable);
}
