package page.clab.api.domain.review.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.activityGroup.application.ActivityGroupMemberService;
import page.clab.api.domain.activityGroup.domain.ActivityGroup;
import page.clab.api.domain.activityGroup.domain.ActivityGroupRole;
import page.clab.api.domain.activityGroup.domain.GroupMember;
import page.clab.api.domain.activityGroup.exception.ActivityGroupNotFinishedException;
import page.clab.api.domain.member.application.MemberLookupService;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.notification.application.NotificationSenderService;
import page.clab.api.domain.review.dao.ReviewRepository;
import page.clab.api.domain.review.domain.Review;
import page.clab.api.domain.review.dto.request.ReviewRequestDto;
import page.clab.api.domain.review.exception.AlreadyReviewedException;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class CreateReviewServiceImpl implements CreateReviewService {

    private final MemberLookupService memberLookupService;
    private final ActivityGroupMemberService activityGroupMemberService;
    private final NotificationSenderService notificationService;
    private final ValidationService validationService;
    private final ReviewRepository reviewRepository;

    @Transactional
    @Override
    public Long execute(ReviewRequestDto requestDto) {
        Member currentMember = memberLookupService.getCurrentMember();
        ActivityGroup activityGroup = activityGroupMemberService.getActivityGroupByIdOrThrow(requestDto.getActivityGroupId());
        validateReviewCreationPermission(activityGroup, currentMember);
        Review review = ReviewRequestDto.toEntity(requestDto, currentMember, activityGroup);
        validationService.checkValid(review);
        notifyGroupLeaderOfNewReview(activityGroup, currentMember);
        return reviewRepository.save(review).getId();
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
        return reviewRepository.existsByMemberAndActivityGroup(member, activityGroup);
    }
}
