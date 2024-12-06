package page.clab.api.global.util;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.PathBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

/**
 * {@code OrderSpecifierUtil} 클래스는 QueryDSL을 사용하여 정렬 조건을 동적으로 생성하는 유틸리티 메서드를 제공합니다.
 * 이 클래스는 {@link Pageable} 객체에서 제공된 정렬 정보를 바탕으로 {@link OrderSpecifier} 배열을 생성하여,
 * QueryDSL 쿼리에 활용할 수 있도록 합니다.
 */
@Component
public class OrderSpecifierUtil {

    public static OrderSpecifier<?>[] getOrderSpecifiers(Pageable pageable, EntityPathBase<?> q) {
        return pageable.getSort().stream()
                .map(order -> createOrderSpecifier(order, q))
                .toArray(OrderSpecifier[]::new);
    }

    private static OrderSpecifier<?> createOrderSpecifier(Sort.Order order, EntityPathBase<?> q) {
        Order direction = order.isAscending() ? Order.ASC : Order.DESC;
        String property = order.getProperty();
        PathBuilder<Object> path = new PathBuilder<>(q.getType(), q.getMetadata());
        return new OrderSpecifier(direction, path.get(property));
    }
}
