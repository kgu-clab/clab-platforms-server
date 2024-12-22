package page.clab.api.domain.activity.activitygroup.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupBoardCategory;
import page.clab.api.global.common.file.dto.response.UploadedFileResponseDto;

@Getter
@Builder
public class ActivityGroupBoardChildResponseDto {

    private Long id;
    private String memberId;
    private String memberName;
    private ActivityGroupBoardCategory category;
    private String title;
    private String content;
    private LocalDateTime dueDateTime;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
    private List<UploadedFileResponseDto> files;
    private List<ActivityGroupBoardChildResponseDto> children;
}
