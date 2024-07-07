package page.clab.api.domain.activityPhoto.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import page.clab.api.domain.activityPhoto.application.port.out.RegisterActivityPhotoPort;
import page.clab.api.domain.activityPhoto.application.port.out.RemoveActivityPhotoPort;
import page.clab.api.domain.activityPhoto.application.port.out.RetrieveActivityPhotoPort;
import page.clab.api.domain.activityPhoto.domain.ActivityPhoto;
import page.clab.api.global.exception.NotFoundException;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ActivityPhotoPersistenceAdapter implements
        RegisterActivityPhotoPort,
        RetrieveActivityPhotoPort,
        RemoveActivityPhotoPort {

    private final ActivityPhotoRepository activityPhotoRepository;

    @Override
    public ActivityPhoto save(ActivityPhoto activityPhoto) {
        return activityPhotoRepository.save(activityPhoto);
    }

    @Override
    public Optional<ActivityPhoto> findById(Long activityPhotoId) {
        return activityPhotoRepository.findById(activityPhotoId);
    }

    @Override
    public ActivityPhoto findByIdOrThrow(Long activityPhotoId) {
        return activityPhotoRepository.findById(activityPhotoId)
                .orElseThrow(() -> new NotFoundException("[ActivityPhoto] id: " + activityPhotoId + "에 해당하는 활동 사진이 존재하지 않습니다."));
    }

    @Override
    public Page<ActivityPhoto> findByConditions(Boolean isPublic, Pageable pageable) {
        return activityPhotoRepository.findByConditions(isPublic, pageable);
    }

    @Override
    public void delete(ActivityPhoto activityPhoto) {
        activityPhotoRepository.delete(activityPhoto);
    }
}
