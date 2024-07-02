package page.clab.api.domain.review.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.review.domain.Review;

public interface RetrieveDeletedReviewsPort {
    Page<Review> findAllByIsDeletedTrue(Pageable pageable);
}