package page.clab.api.domain.members.activityPhoto.application.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import page.clab.api.global.common.file.dto.response.UploadedFileResponseDto;

@Getter
@Builder
public class ActivityPhotoResponseDto {

    private Long id;
    private String title;
    private List<UploadedFileResponseDto> files;
    private LocalDate date;
    private Boolean isPublic;
    private LocalDateTime createdAt;
}
