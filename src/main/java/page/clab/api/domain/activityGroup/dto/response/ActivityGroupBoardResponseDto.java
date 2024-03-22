package page.clab.api.domain.activityGroup.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.activityGroup.domain.ActivityGroupBoard;
import page.clab.api.domain.activityGroup.domain.ActivityGroupBoardCategory;
import page.clab.api.global.common.file.dto.response.UploadedFileResponseDto;

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

    private LocalDateTime updatedAt;

    public static ActivityGroupBoardResponseDto toDto(ActivityGroupBoard board) {
        return ActivityGroupBoardResponseDto.builder()
                .id(board.getId())
                .parentId(board.getParent() != null ? board.getParent().getId() : null)
                .category(board.getCategory())
                .title(board.getTitle())
                .content(board.getContent())
                .files(UploadedFileResponseDto.toDto(board.getUploadedFiles()))
                .dueDateTime(board.getDueDateTime())
                .createdAt(board.getCreatedAt())
                .updatedAt(board.getUpdatedAt())
                .build();
    }

}
