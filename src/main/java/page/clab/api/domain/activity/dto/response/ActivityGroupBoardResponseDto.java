package page.clab.api.domain.activity.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.activity.domain.ActivityGroupBoard;
import page.clab.api.domain.activity.domain.ActivityGroupBoardCategory;
import page.clab.api.global.common.file.dto.response.UploadedFileResponseDto;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ActivityGroupBoardResponseDto {

    private Long id;

    private Long parentId;

    private ActivityGroupBoardCategory category;

    private String title;

    private String content;

    private List<UploadedFileResponseDto> files;

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
