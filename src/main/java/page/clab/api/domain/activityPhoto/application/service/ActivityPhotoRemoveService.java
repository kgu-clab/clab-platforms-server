package page.clab.api.domain.activityPhoto.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.activityPhoto.application.port.in.RemoveActivityPhotoUseCase;
import page.clab.api.domain.activityPhoto.application.port.out.RegisterActivityPhotoPort;
import page.clab.api.domain.activityPhoto.application.port.out.RetrieveActivityPhotoPort;
import page.clab.api.domain.activityPhoto.domain.ActivityPhoto;

@Service
@RequiredArgsConstructor
public class ActivityPhotoRemoveService implements RemoveActivityPhotoUseCase {

    private final RetrieveActivityPhotoPort retrieveActivityPhotoPort;
    private final RegisterActivityPhotoPort registerActivityPhotoPort;

    @Transactional
    @Override
    public Long remove(Long activityPhotoId) {
        ActivityPhoto activityPhoto = retrieveActivityPhotoPort.findByIdOrThrow(activityPhotoId);
        activityPhoto.delete();
        return registerActivityPhotoPort.save(activityPhoto).getId();
    }
}
