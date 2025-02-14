package page.clab.api.domain.activity.review.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.activity.review.application.dto.response.ReviewResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface RetrieveDeletedReviewsUseCase {

    PagedResponseDto<ReviewResponseDto> retrieveDeletedReviews(Pageable pageable);
}
