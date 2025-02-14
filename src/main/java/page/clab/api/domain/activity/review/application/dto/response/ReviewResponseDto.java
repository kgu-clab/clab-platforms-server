package page.clab.api.domain.activity.review.application.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

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
}
