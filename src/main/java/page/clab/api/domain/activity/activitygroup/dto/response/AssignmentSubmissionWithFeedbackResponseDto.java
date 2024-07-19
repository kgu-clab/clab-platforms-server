package page.clab.api.domain.activity.activitygroup.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupBoard;
import page.clab.api.global.common.file.dto.response.UploadedFileResponseDto;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class AssignmentSubmissionWithFeedbackResponseDto {

    private Long id;
    private Long parentId;
    private String content;
    private List<UploadedFileResponseDto> files;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<FeedbackResponseDto> feedbacks;

    public static AssignmentSubmissionWithFeedbackResponseDto toDto(ActivityGroupBoard board, List<FeedbackResponseDto> feedbackDtos) {
        return AssignmentSubmissionWithFeedbackResponseDto.builder()
                .id(board.getId())
                .parentId(board.getParent() != null ? board.getParent().getId() : null)
                .content(board.getContent())
                .files(UploadedFileResponseDto.toDto(board.getUploadedFiles()))
                .createdAt(board.getCreatedAt())
                .updatedAt(board.getUpdatedAt())
                .feedbacks(feedbackDtos)
                .build();
    }
}
