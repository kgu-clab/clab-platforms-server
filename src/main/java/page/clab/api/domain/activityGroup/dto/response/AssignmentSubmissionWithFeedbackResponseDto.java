package page.clab.api.domain.activityGroup.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.activityGroup.domain.ActivityGroupBoard;
import page.clab.api.global.common.file.dto.response.UploadedFileResponseDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssignmentSubmissionWithFeedbackResponseDto {

    private Long id;

    private Long parentId;

    private String content;

    private List<UploadedFileResponseDto> files = new ArrayList<>();

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<FeedbackResponseDto> feedbacks = new ArrayList<>();

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
