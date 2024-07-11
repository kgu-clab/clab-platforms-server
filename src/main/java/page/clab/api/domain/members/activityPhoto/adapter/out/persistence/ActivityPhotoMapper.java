package page.clab.api.domain.members.activityPhoto.adapter.out.persistence;

import org.springframework.stereotype.Component;
import page.clab.api.domain.members.activityPhoto.domain.ActivityPhoto;

@Component
public class ActivityPhotoMapper {

    public ActivityPhotoJpaEntity toJpaEntity(ActivityPhoto activityPhoto) {
        return ActivityPhotoJpaEntity.builder()
                .id(activityPhoto.getId())
                .title(activityPhoto.getTitle())
                .uploadedFiles(activityPhoto.getUploadedFiles())
                .date(activityPhoto.getDate())
                .isPublic(activityPhoto.getIsPublic())
                .isDeleted(activityPhoto.isDeleted())
                .build();
    }

    public ActivityPhoto toDomain(ActivityPhotoJpaEntity entity) {
        return ActivityPhoto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .uploadedFiles(entity.getUploadedFiles())
                .date(entity.getDate())
                .isPublic(entity.getIsPublic())
                .isDeleted(entity.isDeleted())
                .build();
    }
}
