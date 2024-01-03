package page.clab.api.type.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.type.entity.ActivityGroupBoard;
import page.clab.api.util.ModelMapperUtil;

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
