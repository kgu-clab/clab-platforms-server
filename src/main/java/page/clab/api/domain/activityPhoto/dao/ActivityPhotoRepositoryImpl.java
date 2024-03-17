package page.clab.api.domain.activityPhoto.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.activityPhoto.domain.ActivityPhoto;
import page.clab.api.domain.activityPhoto.domain.QActivityPhoto;

import java.util.List;

public class ActivityPhotoRepositoryImpl implements ActivityPhotoRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ActivityPhotoRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<ActivityPhoto> findByConditions(Boolean isPublic, Pageable pageable) {
        QActivityPhoto activityPhoto = QActivityPhoto.activityPhoto;
        BooleanBuilder builder = new BooleanBuilder();

        if (isPublic != null) builder.and(activityPhoto.isPublic.eq(isPublic));

        List<ActivityPhoto> photos = queryFactory.selectFrom(activityPhoto)
                .where(builder)
                .orderBy(activityPhoto.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.selectFrom(activityPhoto)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(photos, pageable, total);
    }

}