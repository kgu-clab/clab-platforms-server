package page.clab.api.domain.members.activityPhoto.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import page.clab.api.domain.members.activityPhoto.domain.ActivityPhoto;
import page.clab.api.global.common.file.domain.UploadedFile;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ActivityPhotoRequestDto {

    @NotNull(message = "{notNull.activityPhoto.title}")
    @Schema(description = "활동 사진 제목", example = "2021년 1월 1일 활동 사진", required = true)
    private String title;

    @NotNull(message = "{notNull.activityPhoto.imageUrl}")
    @Schema(description = "활동 사진 파일 경로 리스트", example = "[\"/resources/files/activity-photos/339609571877700_4305d83e-090a-480b-a470-b5e96164d113.png\", \"/resources/files/activity-photos/4305d83e-090a-480b-a470-b5e96164d114.png\"]", required = true)
    private List<String> fileUrlList;

    @NotNull(message = "{notNull.activityPhoto.date}")
    @Schema(description = "활동 날짜", example = "2021-01-01", required = true)
    private LocalDate date;

    public static ActivityPhoto toEntity(ActivityPhotoRequestDto requestDto, List<UploadedFile> uploadedFiles) {
        return ActivityPhoto.builder()
                .title(requestDto.getTitle())
                .uploadedFiles(uploadedFiles)
                .date(requestDto.getDate())
                .isPublic(false)
                .isDeleted(false)
                .build();
    }
}
