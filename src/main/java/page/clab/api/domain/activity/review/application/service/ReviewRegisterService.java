package page.clab.api.domain.activity.review.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import page.clab.api.domain.activity.activitygroup.application.ActivityGroupMemberService;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroup;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupRole;
import page.clab.api.domain.activity.activitygroup.domain.GroupMember;
import page.clab.api.domain.activity.review.application.dto.mapper.ReviewDtoMapper;
import page.clab.api.domain.activity.review.application.dto.request.ReviewRequestDto;
import page.clab.api.domain.activity.review.application.port.in.RegisterReviewUseCase;
import page.clab.api.domain.activity.review.application.port.out.RegisterReviewPort;
import page.clab.api.domain.activity.review.application.port.out.RetrieveReviewPort;
import page.clab.api.domain.activity.review.domain.Review;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberBasicInfoDto;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.external.memberManagement.notification.application.port.ExternalSendNotificationUseCase;
import page.clab.api.global.exception.BaseException;
import page.clab.api.global.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class ReviewRegisterService implements RegisterReviewUseCase {

    private final RegisterReviewPort registerReviewPort;
    private final RetrieveReviewPort retrieveReviewPort;
    private final ActivityGroupMemberService activityGroupMemberService;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;
    private final ExternalSendNotificationUseCase externalSendNotificationUseCase;
    private final ReviewDtoMapper mapper;

    @Transactional
    @Override
    public Long registerReview(ReviewRequestDto requestDto) {
        MemberBasicInfoDto currentMemberInfo = externalRetrieveMemberUseCase.getCurrentMemberBasicInfo();
        ActivityGroup activityGroup = activityGroupMemberService.getActivityGroupById(requestDto.getActivityGroupId());
        validateReviewCreationPermission(activityGroup, currentMemberInfo.getMemberId());
        Review review = mapper.fromDto(requestDto, currentMemberInfo.getMemberId(), activityGroup);
        notifyGroupLeaderOfNewReview(activityGroup, currentMemberInfo.getMemberName());
        return registerReviewPort.save(review).getId();
    }

    private void validateReviewCreationPermission(ActivityGroup activityGroup, String memberId) {
        if (!activityGroup.isEnded()) {
            throw new BaseException(ErrorCode.ACTIVITY_GROUP_NOT_FINISHED);
        }
        if (isExistsByMemberIdAndActivityGroup(memberId, activityGroup)) {
            throw new BaseException(ErrorCode.ALREADY_REVIEWED);
        }
    }

    private void notifyGroupLeaderOfNewReview(ActivityGroup activityGroup, String memberName) {
        List<GroupMember> groupLeaders = activityGroupMemberService.getGroupMemberByActivityGroupIdAndRole(
            activityGroup.getId(), ActivityGroupRole.LEADER);
        if (!CollectionUtils.isEmpty(groupLeaders)) {
            groupLeaders.forEach(
                leader -> externalSendNotificationUseCase.sendNotificationToMember(leader.getMemberId(),
                    "[" + activityGroup.getName() + "] " + memberName + "님이 리뷰를 등록하였습니다."));
        }
    }

    private boolean isExistsByMemberIdAndActivityGroup(String memberId, ActivityGroup activityGroup) {
        return retrieveReviewPort.existsByMemberIdAndActivityGroup(memberId, activityGroup);
    }
}
