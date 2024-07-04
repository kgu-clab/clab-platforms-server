package page.clab.api.global.util;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.PathBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

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
