package page.clab.api.domain.activityGroup.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.activityGroup.domain.ActivityGroupReport;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityGroupReportResponseDto {

    private Long activityGroupId;

    private String activityGroupName;

    private Long turn;

    private String title;

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static ActivityGroupReportResponseDto toDto(ActivityGroupReport report) {
        return ActivityGroupReportResponseDto.builder()
                .activityGroupId(report.getActivityGroup().getId())
                .activityGroupName(report.getActivityGroup().getName())
                .turn(report.getTurn())
                .title(report.getTitle())
                .content(report.getContent())
                .createdAt(report.getCreatedAt())
                .updatedAt(report.getUpdatedAt())
                .build();
    }

}
