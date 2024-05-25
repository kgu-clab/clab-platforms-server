package page.clab.api.global.util;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class OrderSpecifierUtil {

    public static OrderSpecifier<?>[] getOrderSpecifiers(Pageable pageable, String tableName) {
        List<OrderSpecifier> orderSpecifierList = new ArrayList<>();

        pageable.getSort().stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String property = order.getProperty();
            PathBuilder path = new PathBuilder(Object.class, tableName);
            orderSpecifierList.add(new OrderSpecifier(direction, path.get(property)));
        });
        return orderSpecifierList.toArray(new OrderSpecifier[0]);
    }

}
