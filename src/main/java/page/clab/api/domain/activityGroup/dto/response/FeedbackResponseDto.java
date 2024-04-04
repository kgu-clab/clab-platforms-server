package page.clab.api.domain.activityGroup.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import page.clab.api.domain.activityGroup.domain.ActivityGroupBoard;
import page.clab.api.global.common.file.dto.response.UploadedFileResponseDto;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class FeedbackResponseDto {

    private Long id;

    private String content;

    private List<UploadedFileResponseDto> files;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static FeedbackResponseDto toDto(ActivityGroupBoard board) {
        return FeedbackResponseDto.builder()
                .id(board.getId())
                .content(board.getContent())
                .files(UploadedFileResponseDto.toDto(board.getUploadedFiles()))
                .createdAt(board.getCreatedAt())
                .updatedAt(board.getUpdatedAt())
                .build();
    }

}