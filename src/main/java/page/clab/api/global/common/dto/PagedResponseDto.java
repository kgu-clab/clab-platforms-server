package page.clab.api.global.common.dto;

import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import page.clab.api.global.exception.InvalidColumnException;

import java.util.Comparator;
import java.util.List;

/**
 * 페이지네이션 응답을 위한 제네릭 DTO 클래스입니다. 현재 페이지, 총 아이템 수 및
 * 페이지 관련 정보와 실제 콘텐츠를 제공합니다.
 *
 * @param <T> 페이지네이션 응답의 콘텐츠 타입
 */
@Getter
public class PagedResponseDto<T> {

    /**
     * 현재 페이지 번호 (0부터 시작하는 인덱스).
     */
    private final int currentPage;

    /**
     * 이전 페이지가 있는지 여부를 나타냅니다.
     */
    private final boolean hasPrevious;

    /**
     * 다음 페이지가 있는지 여부를 나타냅니다.
     */
    private final boolean hasNext;

    /**
     * 전체 페이지 수.
     */
    private final int totalPages;

    /**
     * 전체 아이템 수.
     */
    private final long totalItems;

    /**
     * 현재 페이지의 아이템 수.
     */
    private final int take;

    /**
     * 현재 페이지의 콘텐츠 아이템 리스트.
     */
    private final List<T> items;

    /**
     * Page 객체를 사용하여 PagedResponseDto를 생성하는 생성자입니다.
     *
     * @param page 페이지네이션된 데이터를 포함하는 Page 객체
     */
    public PagedResponseDto(Page<T> page) {
        this.currentPage = page.getNumber();
        this.hasPrevious = page.hasPrevious();
        this.hasNext = page.hasNext();
        this.totalPages = page.getTotalPages();
        this.totalItems = page.getTotalElements();
        this.take = page.getNumberOfElements();
        this.items = page.getContent();
    }

    /**
     * totalItems 및 numberOfElements를 추가로 설정할 수 있는 생성자입니다.
     *
     * @param page 페이지네이션된 데이터를 포함하는 Page 객체
     * @param totalItems 전체 아이템 수
     * @param numberOfElements 현재 페이지의 아이템 수
     */
    public PagedResponseDto(Page<T> page, long totalItems, int numberOfElements) {
        this.currentPage = page.getNumber();
        this.hasPrevious = page.hasPrevious();
        this.hasNext = page.hasNext();
        this.totalPages = page.getTotalPages();
        this.totalItems = totalItems;
        this.take = numberOfElements;
        this.items = page.getContent();
    }

    /**
     * List와 Pageable 객체를 사용하여 PagedResponseDto를 생성하는 생성자입니다.
     * 페이지네이션과 정렬이 적용됩니다.
     *
     * @param ts 콘텐츠 아이템 리스트
     * @param pageable 페이지네이션 정보와 정렬 기준을 포함하는 Pageable 객체
     * @param size 전체 아이템 수
     */
    public PagedResponseDto(List<T> ts, Pageable pageable, int size) {
        this.currentPage = pageable.getPageNumber();
        this.hasPrevious = pageable.getPageNumber() > 0;
        this.hasNext = ts.size() == size;
        this.totalPages = (size != 0) ? (int) Math.ceil((double) ts.size() / pageable.getPageSize()) : 0;
        this.totalItems = ts.size();
        this.take = size;
        this.items = applySortingIfNecessary(ts, pageable.getSort());
    }

    /**
     * 정렬이 필요한 경우 아이템 리스트에 정렬을 적용하는 메서드입니다.
     *
     * @param items 정렬되지 않은 아이템 리스트
     * @param sort 정렬 기준을 포함한 Sort 객체
     * @return 정렬된 아이템 리스트
     */
    private List<T> applySortingIfNecessary(List<T> items, Sort sort) {
        if (sort.isSorted()) {
            return sortItems(items, sort);
        }
        return items;
    }

    /**
     * Sort 객체를 사용하여 아이템 리스트를 정렬하는 메서드입니다.
     *
     * @param items 정렬되지 않은 아이템 리스트
     * @param sort 정렬 기준을 포함한 Sort 객체
     * @return 정렬된 아이템 리스트
     */
    private List<T> sortItems(List<T> items, Sort sort) {
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
     * 리플렉션을 사용하여 객체의 특정 필드 값을 추출하는 메서드입니다.
     *
     * @param item 값을 추출할 객체
     * @param fieldName 추출할 필드 이름
     * @return 추출된 필드 값
     */
    private Object extractFieldValue(T item, String fieldName) throws InvalidColumnException {
        try {
            var field = item.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(item);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new InvalidColumnException("잘못된 필드 이름: " + fieldName);
        }
    }
}
