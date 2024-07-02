package page.clab.api.domain.activityPhoto.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.activityPhoto.application.port.in.ActivityPhotoRemoveUseCase;
import page.clab.api.domain.activityPhoto.application.port.out.LoadActivityPhotoPort;
import page.clab.api.domain.activityPhoto.application.port.out.RegisterActivityPhotoPort;
import page.clab.api.domain.activityPhoto.domain.ActivityPhoto;

@Service
@RequiredArgsConstructor
public class ActivityPhotoRemoveService implements ActivityPhotoRemoveUseCase {

    private final LoadActivityPhotoPort loadActivityPhotoPort;
    private final RegisterActivityPhotoPort registerActivityPhotoPort;

    @Transactional
    @Override
    public Long remove(Long activityPhotoId) {
        ActivityPhoto activityPhoto = loadActivityPhotoPort.findByIdOrThrow(activityPhotoId);
        activityPhoto.delete();
        return registerActivityPhotoPort.save(activityPhoto).getId();
    }
}
