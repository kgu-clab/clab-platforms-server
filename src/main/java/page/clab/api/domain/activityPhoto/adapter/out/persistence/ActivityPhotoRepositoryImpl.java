package page.clab.api.domain.activityPhoto.adapter.out.persistence;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ActivityPhotoRepositoryImpl implements ActivityPhotoRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ActivityPhotoJpaEntity> findByConditions(Boolean isPublic, Pageable pageable) {
        QActivityPhotoJpaEntity activityPhoto = QActivityPhotoJpaEntity.activityPhotoJpaEntity;
        BooleanBuilder builder = new BooleanBuilder();

        if (isPublic != null) builder.and(activityPhoto.isPublic.eq(isPublic));

        List<ActivityPhotoJpaEntity> photos = queryFactory.selectFrom(activityPhoto)
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
