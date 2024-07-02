package page.clab.api.domain.review.application.port.out;

import page.clab.api.domain.activityGroup.domain.ActivityGroup;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.review.domain.Review;

public interface RegisterReviewPort {
    Review save(Review review);
    boolean existsByMemberAndActivityGroup(Member member, ActivityGroup activityGroup);
}