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
import page.clab.api.domain.review.application.port.in.RegisterReviewUseCase;
import page.clab.api.domain.review.application.port.out.RegisterReviewPort;
import page.clab.api.domain.review.application.port.out.RetrieveReviewPort;
import page.clab.api.domain.review.domain.Review;
import page.clab.api.domain.review.dto.request.ReviewRequestDto;
import page.clab.api.domain.review.exception.AlreadyReviewedException;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class ReviewRegisterService implements RegisterReviewUseCase {

    private final RetrieveMemberUseCase retrieveMemberUseCase;
    private final ActivityGroupMemberService activityGroupMemberService;
    private final SendNotificationUseCase notificationService;
    private final ValidationService validationService;
    private final RegisterReviewPort registerReviewPort;
    private final RetrieveReviewPort retrieveReviewPort;

    @Transactional
    @Override
    public Long register(ReviewRequestDto requestDto) {
        Member currentMember = retrieveMemberUseCase.getCurrentMember();
        ActivityGroup activityGroup = activityGroupMemberService.getActivityGroupByIdOrThrow(requestDto.getActivityGroupId());
        validateReviewCreationPermission(activityGroup, currentMember);
        Review review = ReviewRequestDto.toEntity(requestDto, currentMember, activityGroup);
        validationService.checkValid(review);
        notifyGroupLeaderOfNewReview(activityGroup, currentMember);
        return registerReviewPort.save(review).getId();
    }

    private void validateReviewCreationPermission(ActivityGroup activityGroup, Member member) {
        if (!activityGroup.isEnded()) {
            throw new ActivityGroupNotFinishedException("활동이 종료된 활동 그룹만 리뷰를 작성할 수 있습니다.");
        }
        if (isExistsByMemberAndActivityGroup(member, activityGroup)) {
            throw new AlreadyReviewedException("이미 리뷰를 작성한 활동 그룹입니다.");
        }
    }

    private void notifyGroupLeaderOfNewReview(ActivityGroup activityGroup, Member member) {
        GroupMember groupLeader = activityGroupMemberService.getGroupMemberByActivityGroupIdAndRole(activityGroup.getId(), ActivityGroupRole.LEADER);
        if (groupLeader != null) {
            notificationService.sendNotificationToMember(groupLeader.getMember().getId(), "[" + activityGroup.getName() + "] " + member.getName() + "님이 리뷰를 등록하였습니다.");
        }
    }

    private boolean isExistsByMemberAndActivityGroup(Member member, ActivityGroup activityGroup) {
        return retrieveReviewPort.existsByMemberAndActivityGroup(member, activityGroup);
    }
}
