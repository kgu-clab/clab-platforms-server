package page.clab.api.domain.activity.review.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroup;
import page.clab.api.domain.activity.review.domain.Review;
import page.clab.api.domain.member.domain.Member;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReviewResponseDto {

    private Long id;
    private Long activityGroupId;
    private String activityGroupName;
    private String activityGroupCategory;
    private String memberId;
    private String name;
    private String department;
    private String content;
    private Boolean isPublic;
    private Boolean isOwner;
    private LocalDateTime createdAt;

    public static ReviewResponseDto toDto(Review review, Member reviewer, boolean isOwner) {
        ActivityGroup activityGroup = review.getActivityGroup();
        return ReviewResponseDto.builder()
                .id(review.getId())
                .activityGroupId(activityGroup.getId())
                .activityGroupName(activityGroup.getName())
                .activityGroupCategory(String.valueOf(activityGroup.getCategory()))
                .memberId(reviewer.getId())
                .name(reviewer.getName())
                .department(reviewer.getDepartment())
                .content(review.getContent())
                .isPublic(review.getIsPublic())
                .isOwner(isOwner)
                .createdAt(review.getCreatedAt())
                .build();
    }
}
