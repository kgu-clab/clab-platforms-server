package page.clab.api.domain.review.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.activityGroup.domain.ActivityGroup;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.review.domain.Review;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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

    public static ReviewResponseDto toDto(Review review, Member currentMember) {
        ActivityGroup activityGroup = review.getActivityGroup();
        Member member = review.getMember();
        return ReviewResponseDto.builder()
                .id(review.getId())
                .activityGroupId(activityGroup.getId())
                .activityGroupName(activityGroup.getName())
                .activityGroupCategory(String.valueOf(activityGroup.getCategory()))
                .memberId(member.getId())
                .name(member.getName())
                .department(member.getDepartment())
                .content(review.getContent())
                .isPublic(review.getIsPublic())
                .isOwner(review.isOwner(currentMember))
                .createdAt(review.getCreatedAt())
                .build();
    }

}
