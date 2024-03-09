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

    private LocalDateTime updateTime;

    private List<FeedbackResponseDto> feedbacks = new ArrayList<>();

    public static AssignmentSubmissionWithFeedbackResponseDto of(ActivityGroupBoard activityGroupBoard) {
        return AssignmentSubmissionWithFeedbackResponseDto.builder()
                .id(activityGroupBoard.getId())
                .parentId(activityGroupBoard.getParent() != null ? activityGroupBoard.getParent().getId() : null)
                .content(activityGroupBoard.getContent())
                .files(activityGroupBoard.getUploadedFiles().stream().map(UploadedFileResponseDto::of).toList())
                .createdAt(activityGroupBoard.getCreatedAt())
                .updateTime(activityGroupBoard.getUpdateTime())
                .build();
    }

}
