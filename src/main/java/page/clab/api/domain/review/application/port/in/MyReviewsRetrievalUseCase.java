package page.clab.api.domain.review.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.review.dto.response.ReviewResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface MyReviewsRetrievalUseCase {
    PagedResponseDto<ReviewResponseDto> retrieve(Pageable pageable);
}
