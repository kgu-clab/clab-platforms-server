package page.clab.api.domain.activityGroup.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import page.clab.api.domain.activityGroup.domain.ActivityGroupBoard;
import page.clab.api.domain.activityGroup.domain.ActivityGroupBoardCategory;
import page.clab.api.global.common.file.dto.response.UploadedFileResponseDto;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class ActivityGroupBoardChildResponseDto {

    private Long id;

    private ActivityGroupBoardCategory category;

    private String title;

    private String content;

    private LocalDateTime dueDateTime;

    private LocalDateTime updatedAt;

    private LocalDateTime createdAt;

    private List<UploadedFileResponseDto> files;

    private List<ActivityGroupBoardChildResponseDto> children;

    public static ActivityGroupBoardChildResponseDto toDto(ActivityGroupBoard board) {
        return ActivityGroupBoardChildResponseDto.builder()
                .id(board.getId())
                .category(board.getCategory())
                .title(board.getTitle())
                .content(board.getContent())
                .dueDateTime(board.getDueDateTime())
                .updatedAt(board.getUpdatedAt())
                .createdAt(board.getCreatedAt())
                .files(UploadedFileResponseDto.toDto(board.getUploadedFiles()))
                .children(board.getChildren().stream().map(ActivityGroupBoardChildResponseDto::toDto).toList())
                .build();
    }

}
