package page.clab.api.domain.activityPhoto.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.activityPhoto.domain.ActivityPhoto;
import page.clab.api.global.common.file.dto.response.UploadedFileResponseDto;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class ActivityPhotoResponseDto {

    private Long id;

    private String title;

    private List<UploadedFileResponseDto> files;

    private LocalDate date;

    private Boolean isPublic;

    public static ActivityPhotoResponseDto toDto(ActivityPhoto activityPhoto) {
        return ActivityPhotoResponseDto.builder()
                .id(activityPhoto.getId())
                .title(activityPhoto.getTitle())
                .files(UploadedFileResponseDto.toDto(activityPhoto.getUploadedFiles()))
                .date(activityPhoto.getDate())
                .isPublic(activityPhoto.getIsPublic())
                .build();
    }
}
