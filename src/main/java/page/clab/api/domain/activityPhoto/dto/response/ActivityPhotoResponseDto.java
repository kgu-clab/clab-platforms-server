package page.clab.api.domain.activityPhoto.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.activityPhoto.domain.ActivityPhoto;
import page.clab.api.global.common.file.dto.response.UploadedFileResponseDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityPhotoResponseDto {

    private Long id;

    private String title;

    private List<UploadedFileResponseDto> files = new ArrayList<>();

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
