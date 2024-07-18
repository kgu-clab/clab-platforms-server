package page.clab.api.domain.activity.activitygroup.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupReport;

import java.time.LocalDateTime;

@Getter
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
