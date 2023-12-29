package page.clab.api.type.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.type.entity.ActivityGroupBoard;
import page.clab.api.util.ModelMapperUtil;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityGroupBoardChildResponseDto {

    private String category;

    private String title;

    private String content;

    private String filePath;

    private String fileName;

    private List<ActivityGroupBoardChildResponseDto> children;

    public static ActivityGroupBoardChildResponseDto of(ActivityGroupBoard activityGroupBoard) {
        ActivityGroupBoardChildResponseDto activityGroupBoardResponseDto = ModelMapperUtil.getModelMapper().map(activityGroupBoard, ActivityGroupBoardChildResponseDto.class);
        if (activityGroupBoard.getChildren() != null && !activityGroupBoard.getChildren().isEmpty()) {
            List<ActivityGroupBoardChildResponseDto> childrenDtoList = new ArrayList<>();
            for (ActivityGroupBoard child : activityGroupBoard.getChildren()) {
                childrenDtoList.add(ActivityGroupBoardChildResponseDto.of(child));
            }
            activityGroupBoardResponseDto.setChildren(childrenDtoList);
        }
        return activityGroupBoardResponseDto;
    }

}
