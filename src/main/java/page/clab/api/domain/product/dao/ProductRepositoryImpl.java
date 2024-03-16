package page.clab.api.domain.product.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.product.domain.Product;
import page.clab.api.domain.product.domain.QProduct;

import java.util.List;

public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ProductRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<Product> findByConditions(String productName, Pageable pageable) {
        QProduct qProduct = QProduct.product;
        BooleanBuilder builder = new BooleanBuilder();

        if (productName != null && !productName.isEmpty()) {
            builder.and(qProduct.name.containsIgnoreCase(productName));
        }

        List<Product> products = queryFactory.selectFrom(qProduct)
                .where(builder)
                .orderBy(qProduct.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long count = queryFactory.query().from(qProduct)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(products, pageable, count);
    }

}