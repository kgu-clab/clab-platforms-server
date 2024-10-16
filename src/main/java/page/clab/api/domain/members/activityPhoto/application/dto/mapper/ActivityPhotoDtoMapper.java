package page.clab.api.domain.members.activityPhoto.application.dto.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import page.clab.api.domain.members.activityPhoto.application.dto.request.ActivityPhotoRequestDto;
import page.clab.api.domain.members.activityPhoto.application.dto.response.ActivityPhotoResponseDto;
import page.clab.api.domain.members.activityPhoto.domain.ActivityPhoto;
import page.clab.api.global.common.file.domain.UploadedFile;
import page.clab.api.global.common.file.dto.mapper.FileDtoMapper;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ActivityPhotoDtoMapper {

    private final FileDtoMapper mapper;

    public ActivityPhoto fromDto(ActivityPhotoRequestDto requestDto, List<UploadedFile> uploadedFiles) {
        return ActivityPhoto.builder()
                .title(requestDto.getTitle())
                .uploadedFiles(uploadedFiles)
                .date(requestDto.getDate())
                .isPublic(false)
                .isDeleted(false)
                .build();
    }

    public ActivityPhotoResponseDto toDto(ActivityPhoto activityPhoto) {
        return ActivityPhotoResponseDto.builder()
                .id(activityPhoto.getId())
                .title(activityPhoto.getTitle())
                .files(mapper.toDto(activityPhoto.getUploadedFiles()))
                .date(activityPhoto.getDate())
                .isPublic(activityPhoto.getIsPublic())
                .createdAt(activityPhoto.getCreatedAt())
                .build();
    }
}
