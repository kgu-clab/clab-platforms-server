package page.clab.api.domain.review.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.review.domain.Review;
import page.clab.api.global.util.ModelMapperUtil;

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

    private LocalDateTime createdAt;

    public static ReviewResponseDto of(Review review) {
        ReviewResponseDto reviewResponseDto = ModelMapperUtil.getModelMapper().map(review, ReviewResponseDto.class);
        reviewResponseDto.setActivityGroupId(review.getActivityGroup().getId());
        reviewResponseDto.setActivityGroupName(review.getActivityGroup().getName());
        reviewResponseDto.setActivityGroupCategory(String.valueOf(review.getActivityGroup().getCategory()));
        reviewResponseDto.setMemberId(review.getMember().getId());
        reviewResponseDto.setName(review.getMember().getName());
        reviewResponseDto.setDepartment(review.getMember().getDepartment());
        return reviewResponseDto;
    }

}
