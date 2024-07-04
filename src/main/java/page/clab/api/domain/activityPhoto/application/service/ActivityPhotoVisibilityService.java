package page.clab.api.domain.activityPhoto.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.activityPhoto.application.port.in.ToggleActivityPhotoVisibilityUseCase;
import page.clab.api.domain.activityPhoto.application.port.out.RegisterActivityPhotoPort;
import page.clab.api.domain.activityPhoto.application.port.out.RetrieveActivityPhotoPort;
import page.clab.api.domain.activityPhoto.domain.ActivityPhoto;

@Service
@RequiredArgsConstructor
public class ActivityPhotoVisibilityService implements ToggleActivityPhotoVisibilityUseCase {

    private final RetrieveActivityPhotoPort retrieveActivityPhotoPort;
    private final RegisterActivityPhotoPort registerActivityPhotoPort;

    @Transactional
    @Override
    public Long toggleActivityPhotoVisibility(Long activityPhotoId) {
        ActivityPhoto activityPhoto = retrieveActivityPhotoPort.findByIdOrThrow(activityPhotoId);
        activityPhoto.togglePublicStatus();
        return registerActivityPhotoPort.save(activityPhoto).getId();
    }
}
