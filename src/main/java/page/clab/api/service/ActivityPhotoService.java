package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.exception.NotFoundException;
import page.clab.api.repository.ActivityPhotoRepository;
import page.clab.api.type.dto.ActivityPhotoRequestDto;
import page.clab.api.type.dto.ActivityPhotoResponseDto;
import page.clab.api.type.dto.PagedResponseDto;
import page.clab.api.type.entity.ActivityPhoto;

@Service
@RequiredArgsConstructor
public class ActivityPhotoService {

    private final ActivityPhotoRepository activityPhotoRepository;

    public Long createActivityPhoto(ActivityPhotoRequestDto activityPhotoRequestDto) {
        ActivityPhoto activityPhoto = ActivityPhoto.of(activityPhotoRequestDto);
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

    private ActivityPhoto getActivityPhotoByIdOrThrow(Long activityPhotoId) {
        return activityPhotoRepository.findById(activityPhotoId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 활동 사진입니다."));
    }

    private Page<ActivityPhoto> getActivityPhotoByIsPublicTrue(Pageable pageable) {
        return activityPhotoRepository.findAllByIsPublicTrueOrderByCreatedAtDesc(pageable);
    }

}
