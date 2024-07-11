package page.clab.api.domain.members.activityPhoto.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ActivityPhotoRepositoryCustom {
    Page<ActivityPhotoJpaEntity> findByConditions(Boolean isPublic, Pageable pageable);
}
