package page.clab.api.domain.activityPhoto.application;

import lombok.RequiredArgsConstructor;
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

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityPhotoService {

    private final ActivityPhotoRepository activityPhotoRepository;

    private final FileService fileService;

    public Long createActivityPhoto(ActivityPhotoRequestDto dto) {
        List<UploadedFile> uploadedFiles = dto.getFileUrlList() != null ?
                dto.getFileUrlList().stream()
                        .map(fileService::getUploadedFileByUrl)
                        .collect(Collectors.toList()) : Collections.emptyList();
        ActivityPhoto activityPhoto = ActivityPhoto.create(dto, uploadedFiles);
        return activityPhotoRepository.save(activityPhoto).getId();
    }

    public PagedResponseDto<ActivityPhotoResponseDto> getActivityPhotosByConditions(Boolean isPublic, Pageable pageable) {
        Page<ActivityPhoto> activityPhotos = activityPhotoRepository.findByConditions(isPublic, pageable);
        return new PagedResponseDto<>(activityPhotos.map(ActivityPhotoResponseDto::of));
    }

    public Long updateActivityPhoto(Long activityPhotoId) {
        ActivityPhoto activityPhoto = getActivityPhotoByIdOrThrow(activityPhotoId);
        activityPhoto.setIsPublic(!activityPhoto.getIsPublic());
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

}
