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

    public static ActivityGroupReportResponseDto of(ActivityGroupReport activityGroupReport) {
        return ActivityGroupReportResponseDto.builder()
                .activityGroupId(activityGroupReport.getActivityGroup().getId())
                .activityGroupName(activityGroupReport.getActivityGroup().getName())
                .turn(activityGroupReport.getTurn())
                .title(activityGroupReport.getTitle())
                .content(activityGroupReport.getContent())
                .createdAt(activityGroupReport.getCreatedAt())
                .updatedAt(activityGroupReport.getUpdatedAt())
                .build();
    }

}
