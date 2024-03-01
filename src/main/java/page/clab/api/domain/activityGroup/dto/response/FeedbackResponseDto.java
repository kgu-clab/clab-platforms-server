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
public class FeedbackResponseDto {

    private Long id;

    private String content;

    private List<UploadedFileResponseDto> files = new ArrayList<>();

    private LocalDateTime createdAt;

    private LocalDateTime updateTime;

    public static FeedbackResponseDto of(ActivityGroupBoard activityGroupBoard) {
        return FeedbackResponseDto.builder()
                .id(activityGroupBoard.getId())
                .content(activityGroupBoard.getContent())
                .files(activityGroupBoard.getUploadedFiles().stream().map(UploadedFileResponseDto::of).toList())
                .createdAt(activityGroupBoard.getCreatedAt())
                .updateTime(activityGroupBoard.getUpdateTime())
                .build();
    }

}