package page.clab.api.domain.activityPhoto.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.activityPhoto.dao.ActivityPhotoRepository;
import page.clab.api.domain.activityPhoto.domain.ActivityPhoto;
import page.clab.api.domain.activityPhoto.dto.request.ActivityPhotoRequestDto;
import page.clab.api.domain.activityPhoto.dto.response.ActivityPhotoResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.file.application.UploadedFileService;
import page.clab.api.global.common.file.domain.UploadedFile;
import page.clab.api.global.exception.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityPhotoService {

    private final ActivityPhotoRepository activityPhotoRepository;

    private final UploadedFileService uploadedFileService;

    public Long createActivityPhoto(ActivityPhotoRequestDto requestDto) {
        List<UploadedFile> uploadedFiles = uploadedFileService.getUploadedFilesByUrls(requestDto.getFileUrlList());
        ActivityPhoto activityPhoto = ActivityPhotoRequestDto.toEntity(requestDto, uploadedFiles);
        return activityPhotoRepository.save(activityPhoto).getId();
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<ActivityPhotoResponseDto> getActivityPhotosByConditions(Boolean isPublic, Pageable pageable) {
        Page<ActivityPhoto> activityPhotos = activityPhotoRepository.findByConditions(isPublic, pageable);
        return new PagedResponseDto<>(activityPhotos.map(ActivityPhotoResponseDto::toDto));
    }

    @Transactional
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

}
