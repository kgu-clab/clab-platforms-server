package page.clab.api.domain.activityGroup.dto.response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.activityGroup.domain.ActivityGroupBoard;
import page.clab.api.domain.activityGroup.domain.ActivityGroupBoardCategory;
import page.clab.api.global.common.file.dto.response.UploadedFileResponseDto;
import page.clab.api.global.util.ModelMapperUtil;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityGroupBoardResponseDto {

    private Long id;

    private ActivityGroupBoardCategory category;

    private String title;

    private String content;

    private List<UploadedFileResponseDto> files = new ArrayList<>();

    private LocalDateTime dueDateTime;

    private LocalDateTime createdAt;

    public static ActivityGroupBoardResponseDto of(ActivityGroupBoard activityGroupBoard) {
        return ModelMapperUtil.getModelMapper().map(activityGroupBoard, ActivityGroupBoardResponseDto.class);
    }

}
