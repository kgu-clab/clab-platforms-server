package page.clab.api.domain.review.application;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.review.dto.response.ReviewResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface FetchMyReviewsService {
    PagedResponseDto<ReviewResponseDto> execute(Pageable pageable);
}
