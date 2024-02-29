package page.clab.api.domain.activityGroup.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.activityGroup.domain.ActivityGroupBoard;
import page.clab.api.domain.activityGroup.domain.ActivityGroupBoardCategory;
import page.clab.api.global.common.file.dto.response.UploadedFileResponseDto;
import page.clab.api.global.util.ModelMapperUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityGroupBoardResponseDto {

    private Long id;

    private Long parentId;

    private ActivityGroupBoardCategory category;

    private String title;

    private String content;

    private List<UploadedFileResponseDto> files = new ArrayList<>();

    private LocalDateTime dueDateTime;

    private LocalDateTime createdAt;

    private LocalDateTime updateTime;

    public static ActivityGroupBoardResponseDto of(ActivityGroupBoard activityGroupBoard) {
        ActivityGroupBoardResponseDto activityGroupBoardResponseDto = ModelMapperUtil.getModelMapper().map(activityGroupBoard, ActivityGroupBoardResponseDto.class);
        activityGroupBoardResponseDto.setParentId(activityGroupBoard.getParent() != null ? activityGroupBoard.getParent().getId() : null);
        return activityGroupBoardResponseDto;
    }

}
