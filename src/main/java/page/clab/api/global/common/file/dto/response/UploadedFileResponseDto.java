package page.clab.api.global.common.file.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UploadedFileResponseDto {

    private String fileUrl;
    private String originalFileName;
    private Long storagePeriod;
    private LocalDateTime createdAt;
}
