package page.clab.api.domain.activity.review.application.dto.mapper;

import org.springframework.stereotype.Component;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroup;
import page.clab.api.domain.activity.review.application.dto.request.ReviewRequestDto;
import page.clab.api.domain.activity.review.application.dto.response.ReviewResponseDto;
import page.clab.api.domain.activity.review.domain.Review;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberReviewInfoDto;

@Component
public class ReviewDtoMapper {

    public Review fromDto(ReviewRequestDto requestDto, String memberId, ActivityGroup activityGroup) {
        return Review.builder()
                .activityGroup(activityGroup)
                .memberId(memberId)
                .content(requestDto.getContent())
                .isPublic(false)
                .isDeleted(false)
                .build();
    }

    public ReviewResponseDto toDto(Review review, MemberReviewInfoDto reviewer, boolean isOwner) {
        ActivityGroup activityGroup = review.getActivityGroup();
        return ReviewResponseDto.builder()
                .id(review.getId())
                .activityGroupId(activityGroup.getId())
                .activityGroupName(activityGroup.getName())
                .activityGroupCategory(String.valueOf(activityGroup.getCategory()))
                .memberId(reviewer.getMemberId())
                .name(reviewer.getMemberName())
                .department(reviewer.getDepartment())
                .content(review.getContent())
                .isPublic(review.getIsPublic())
                .isOwner(isOwner)
                .createdAt(review.getCreatedAt())
                .build();
    }
}
