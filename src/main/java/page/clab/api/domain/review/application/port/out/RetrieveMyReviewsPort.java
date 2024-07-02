package page.clab.api.domain.review.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.review.domain.Review;

public interface RetrieveMyReviewsPort {
    Page<Review> findAllByMember(Member member, Pageable pageable);
}
