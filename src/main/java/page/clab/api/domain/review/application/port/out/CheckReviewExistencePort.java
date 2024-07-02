package page.clab.api.domain.review.application.port.out;

import page.clab.api.domain.activityGroup.domain.ActivityGroup;
import page.clab.api.domain.member.domain.Member;

public interface CheckReviewExistencePort {
    boolean existsByMemberAndActivityGroup(Member member, ActivityGroup activityGroup);
}
