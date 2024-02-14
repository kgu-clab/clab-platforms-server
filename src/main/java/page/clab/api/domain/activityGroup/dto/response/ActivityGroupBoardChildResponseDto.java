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
import page.clab.api.global.util.ModelMapperUtil;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityGroupBoardChildResponseDto {

    private String category;

    private String title;

    private String content;

    private String fileUrl;

    private String fileName;

    private LocalDateTime storageDateTimeOfFile;

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
