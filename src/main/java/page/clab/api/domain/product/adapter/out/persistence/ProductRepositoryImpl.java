package page.clab.api.domain.product.adapter.out.persistence;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.product.domain.Product;
import page.clab.api.domain.product.domain.QProduct;
import page.clab.api.global.util.OrderSpecifierUtil;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Product> findByConditions(String productName, Pageable pageable) {
        QProduct qProduct = QProduct.product;
        BooleanBuilder builder = new BooleanBuilder();

        if (productName != null && !productName.isEmpty()) {
            builder.and(qProduct.name.containsIgnoreCase(productName));
        }

        List<Product> products = queryFactory.selectFrom(qProduct)
                .where(builder)
                .orderBy(OrderSpecifierUtil.getOrderSpecifiers(pageable, qProduct))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long count = queryFactory.query().from(qProduct)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(products, pageable, count);
    }
}
