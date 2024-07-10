package page.clab.api.domain.review.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.activityGroup.application.ActivityGroupMemberService;
import page.clab.api.domain.activityGroup.domain.ActivityGroup;
import page.clab.api.domain.activityGroup.domain.ActivityGroupRole;
import page.clab.api.domain.activityGroup.domain.GroupMember;
import page.clab.api.domain.activityGroup.exception.ActivityGroupNotFinishedException;
import page.clab.api.domain.member.application.port.in.RetrieveMemberUseCase;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.notification.application.port.in.SendNotificationUseCase;
import page.clab.api.domain.review.application.dto.request.ReviewRequestDto;
import page.clab.api.domain.review.application.exception.AlreadyReviewedException;
import page.clab.api.domain.review.application.port.in.RegisterReviewUseCase;
import page.clab.api.domain.review.application.port.out.RegisterReviewPort;
import page.clab.api.domain.review.application.port.out.RetrieveReviewPort;
import page.clab.api.domain.review.domain.Review;

@Service
@RequiredArgsConstructor
public class ReviewRegisterService implements RegisterReviewUseCase {

    private final RetrieveMemberUseCase retrieveMemberUseCase;
    private final ActivityGroupMemberService activityGroupMemberService;
    private final SendNotificationUseCase notificationService;
    private final RegisterReviewPort registerReviewPort;
    private final RetrieveReviewPort retrieveReviewPort;

    @Transactional
    @Override
    public Long registerReview(ReviewRequestDto requestDto) {
        Member currentMember = retrieveMemberUseCase.getCurrentMember();
        ActivityGroup activityGroup = activityGroupMemberService.getActivityGroupByIdOrThrow(requestDto.getActivityGroupId());
        validateReviewCreationPermission(activityGroup, currentMember.getId());
        Review review = ReviewRequestDto.toEntity(requestDto, currentMember.getId(), activityGroup);
        notifyGroupLeaderOfNewReview(activityGroup, currentMember);
        return registerReviewPort.save(review).getId();
    }

    private void validateReviewCreationPermission(ActivityGroup activityGroup, String memberId) {
        if (!activityGroup.isEnded()) {
            throw new ActivityGroupNotFinishedException("활동이 종료된 활동 그룹만 리뷰를 작성할 수 있습니다.");
        }
        if (isExistsByMemberIdAndActivityGroup(memberId, activityGroup)) {
            throw new AlreadyReviewedException("이미 리뷰를 작성한 활동 그룹입니다.");
        }
    }

    private void notifyGroupLeaderOfNewReview(ActivityGroup activityGroup, Member member) {
        GroupMember groupLeader = activityGroupMemberService.getGroupMemberByActivityGroupIdAndRole(activityGroup.getId(), ActivityGroupRole.LEADER);
        if (groupLeader != null) {
            notificationService.sendNotificationToMember(groupLeader.getMemberId(), "[" + activityGroup.getName() + "] " + member.getName() + "님이 리뷰를 등록하였습니다.");
        }
    }

    private boolean isExistsByMemberIdAndActivityGroup(String memberId, ActivityGroup activityGroup) {
        return retrieveReviewPort.existsByMemberIdAndActivityGroup(memberId, activityGroup);
    }
}
