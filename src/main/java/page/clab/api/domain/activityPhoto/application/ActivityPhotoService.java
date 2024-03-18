package page.clab.api.domain.activityPhoto.application;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.domain.activityPhoto.dao.ActivityPhotoRepository;
import page.clab.api.domain.activityPhoto.domain.ActivityPhoto;
import page.clab.api.domain.activityPhoto.dto.request.ActivityPhotoRequestDto;
import page.clab.api.domain.activityPhoto.dto.response.ActivityPhotoResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.file.application.FileService;
import page.clab.api.global.common.file.domain.UploadedFile;
import page.clab.api.global.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityPhotoService {

    private final ActivityPhotoRepository activityPhotoRepository;

    private final FileService fileService;

    public Long createActivityPhoto(ActivityPhotoRequestDto dto) {
        List<UploadedFile> uploadedFiles = prepareUploadedFiles(dto.getFileUrlList());
        ActivityPhoto activityPhoto = ActivityPhoto.create(dto, uploadedFiles);
        return activityPhotoRepository.save(activityPhoto).getId();
    }

    public PagedResponseDto<ActivityPhotoResponseDto> getActivityPhotosByConditions(Boolean isPublic, Pageable pageable) {
        Page<ActivityPhoto> activityPhotos = activityPhotoRepository.findByConditions(isPublic, pageable);
        return new PagedResponseDto<>(activityPhotos.map(ActivityPhotoResponseDto::of));
    }

    public Long togglePublicStatus(Long activityPhotoId) {
        ActivityPhoto activityPhoto = getActivityPhotoByIdOrThrow(activityPhotoId);
        activityPhoto.togglePublicStatus();
        return activityPhotoRepository.save(activityPhoto).getId();
    }

    public Long deleteActivityPhoto(Long activityPhotoId) {
        ActivityPhoto activityPhoto = getActivityPhotoByIdOrThrow(activityPhotoId);
        activityPhotoRepository.delete(activityPhoto);
        return activityPhoto.getId();
    }

    public ActivityPhoto getActivityPhotoByIdOrThrow(Long activityPhotoId) {
        return activityPhotoRepository.findById(activityPhotoId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 활동 사진입니다."));
    }

    @NotNull
    private List<UploadedFile> prepareUploadedFiles(List<String> fileUrls) {
        if (fileUrls == null) return new ArrayList<>();
        return fileUrls.stream()
                .map(fileService::getUploadedFileByUrl)
                .collect(Collectors.toList());
    }

}
