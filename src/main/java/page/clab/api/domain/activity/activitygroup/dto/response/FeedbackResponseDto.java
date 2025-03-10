package page.clab.api.domain.activity.activitygroup.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import page.clab.api.global.common.file.dto.response.UploadedFileResponseDto;

@Getter
@Builder
public class FeedbackResponseDto {

    private Long id;
    private String memberId;
    private String memberName;
    private String content;
    private List<UploadedFileResponseDto> files;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
