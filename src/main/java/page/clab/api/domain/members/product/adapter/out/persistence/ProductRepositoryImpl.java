package page.clab.api.domain.members.product.adapter.out.persistence;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import page.clab.api.global.util.OrderSpecifierUtil;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ProductJpaEntity> findByConditions(String productName, Pageable pageable) {
        QProductJpaEntity product = QProductJpaEntity.productJpaEntity;
        BooleanBuilder builder = new BooleanBuilder();

        if (productName != null && !productName.isEmpty()) {
            builder.and(product.name.containsIgnoreCase(productName));
        }

        List<ProductJpaEntity> products = queryFactory.selectFrom(product)
                .where(builder)
                .orderBy(OrderSpecifierUtil.getOrderSpecifiers(pageable, product))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long count = queryFactory.query().from(product)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(products, pageable, count);
    }
}
