package page.clab.api.domain.activityPhoto.application;

import java.util.List;
import java.util.stream.Collectors;
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

@Service
@RequiredArgsConstructor
public class ActivityPhotoService {

    private final ActivityPhotoRepository activityPhotoRepository;

    private final FileService fileService;

    public Long createActivityPhoto(ActivityPhotoRequestDto activityPhotoRequestDto) {
        ActivityPhoto activityPhoto = ActivityPhoto.of(activityPhotoRequestDto);
        List<String> fileUrls = activityPhotoRequestDto.getFileUrlList();
        if (fileUrls != null) {
            List<UploadedFile> uploadFileList =  fileUrls.stream()
                    .map(fileService::getUploadedFileByUrl)
                    .collect(Collectors.toList());
            activityPhoto.setUploadedFiles(uploadFileList);
        }
        return activityPhotoRepository.save(activityPhoto).getId();
    }

    public PagedResponseDto<ActivityPhotoResponseDto> getActivityPhotos(Pageable pageable) {
        Page<ActivityPhoto> activityPhotos = activityPhotoRepository.findAllByOrderByCreatedAtDesc(pageable);
        return new PagedResponseDto<>(activityPhotos.map(ActivityPhotoResponseDto::of));
    }

    public PagedResponseDto<ActivityPhotoResponseDto> getPublicActivityPhotos(Pageable pageable) {
        Page<ActivityPhoto> activityPhotos = getActivityPhotoByIsPublicTrue(pageable);
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

    public boolean isActivityPhotoExist(Long activityPhotoId) {
        return activityPhotoRepository.existsById(activityPhotoId);
    }

    private Page<ActivityPhoto> getActivityPhotoByIsPublicTrue(Pageable pageable) {
        return activityPhotoRepository.findAllByIsPublicTrueOrderByCreatedAtDesc(pageable);
    }

}
