package page.clab.api.domain.activityGroup.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.activityGroup.domain.ActivityGroupBoard;
import page.clab.api.global.util.ModelMapperUtil;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityGroupBoardResponseDto {

    private String category;

    private String title;

    private String content;

    private String filePath;

    private String fileName;

    public static ActivityGroupBoardResponseDto of(ActivityGroupBoard activityGroupBoard) {
        return ModelMapperUtil.getModelMapper().map(activityGroupBoard, ActivityGroupBoardResponseDto.class);
    }

}
