package page.clab.api.global.util;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import page.clab.api.global.exception.BaseException;
import page.clab.api.global.exception.ErrorCode;

/**
 * {@code PageableUtils} 클래스는 정렬 및 페이지네이션 정보를 기반으로 {@link Pageable} 객체를 생성하는 유틸리티 클래스입니다. 이 클래스는 주어진 정렬 기준이 유효한지 검증하고,
 * 유효하지 않은 경우 예외를 던집니다.
 */
@Component
public class PageableUtils {

    private final ColumnValidator columnValidator;

    public PageableUtils(ColumnValidator columnValidator) {
        this.columnValidator = columnValidator;
    }

    public Pageable createPageable(int page, int size, List<String> sortByList, List<String> sortDirectionList,
        Class<?> domainClass) {
        if (sortByList.size() != sortDirectionList.size()) {
            throw new BaseException(ErrorCode.SORTING_ARGUMENT_MISMATCH);
        }

        for (String sortBy : sortByList) {
            if (!columnValidator.isValidColumn(domainClass, sortBy)) {
                throw new BaseException(ErrorCode.INVALID_COLUMN, "유효하지 않은 컬럼명: " + sortBy);
            }
        }

        for (String direction : sortDirectionList) {
            if (!isValidateSortDirection(direction)) {
                throw new BaseException(ErrorCode.INVALID_SORT_DIRECTION, direction + "은 지원하지 않는 정렬 방식입니다.");
            }
        }

        Sort sort = Sort.by(
            IntStream.range(0, sortByList.size())
                .mapToObj(i -> new Sort.Order(Sort.Direction.fromString(sortDirectionList.get(i)), sortByList.get(i)))
                .collect(Collectors.toList())
        );

        return PageRequest.of(page, size, sort);
    }

    private boolean isValidateSortDirection(String direction) {
        return "asc".equalsIgnoreCase(direction) || "desc".equalsIgnoreCase(direction);
    }
}
