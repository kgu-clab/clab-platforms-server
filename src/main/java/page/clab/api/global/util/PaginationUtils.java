package page.clab.api.global.util;

import java.util.Comparator;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import page.clab.api.global.exception.InvalidColumnException;

/**
 * {@code PaginationUtils} 클래스는 정렬 및 페이지네이션 기능을 통해 리스트에 대한 데이터를 처리하는 유틸리티 클래스입니다.
 */
@Component
public class PaginationUtils {

    /**
     * 아이템 리스트에 정렬을 적용하는 메서드입니다.
     *
     * @param items 정렬되지 않은 아이템 리스트
     * @param sort  정렬 기준을 포함한 Sort 객체
     * @return 정렬된 아이템 리스트
     */
    public static <T> List<T> applySorting(List<T> items, Sort sort) {
        if (!sort.isSorted()) {
            return items;
        }

        Comparator<T> comparator = sort.stream()
            .map(order -> {
                Comparator<T> itemComparator = Comparator.comparing(
                    item -> {
                        try {
                            return (Comparable) extractFieldValue(item, order.getProperty());
                        } catch (InvalidColumnException e) {
                            throw new RuntimeException(e);
                        }
                    }
                );
                return order.isAscending() ? itemComparator : itemComparator.reversed();
            })
            .reduce(Comparator::thenComparing)
            .orElseThrow(IllegalArgumentException::new);

        return items.stream()
            .sorted(comparator)
            .toList();
    }

    /**
     * 아이템 리스트에 슬라이싱을 적용하는 메서드입니다.
     *
     * @param items    슬라이싱되지 않은 아이템 리스트
     * @param pageable 페이지네이션 정보를 포함하는 Pageable 객체
     * @return 슬라이싱된 아이템 리스트
     */
    public static <T> List<T> applySlicing(List<T> items, Pageable pageable) {
        return items.stream()
            .skip(pageable.getOffset())
            .limit(pageable.getPageSize())
            .toList();
    }

    /**
     * 아이템 리스트에 정렬과 슬라이싱을 적용하는 메서드입니다.
     *
     * @param items    정렬 및 슬라이싱 되지 않은 아이템 리스트
     * @param pageable 페이지네이션 정보와 정렬 기준을 포함하는 Pageable 객체
     * @return 정렬 및 슬라이싱된 아이템 리스트
     */
    public static <T> List<T> applySortingAndSlicing(List<T> items, Pageable pageable) {
        List<T> sortedItems = applySorting(items, pageable.getSort());
        return applySlicing(sortedItems, pageable);
    }

    /**
     * 리플렉션을 사용하여 객체의 특정 필드 값을 추출하는 메서드입니다.
     *
     * @param item      값을 추출할 객체
     * @param fieldName 추출할 필드 이름
     * @return 추출된 필드 값
     */
    private static <T> Object extractFieldValue(T item, String fieldName) throws InvalidColumnException {
        try {
            var field = item.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(item);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new InvalidColumnException("잘못된 필드 이름: " + fieldName);
        }
    }
}
