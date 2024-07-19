package page.clab.api.global.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import page.clab.api.global.exception.InvalidColumnException;
import page.clab.api.global.exception.SortingArgumentException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class PageableUtils {

    private final ColumnValidator columnValidator;

    public PageableUtils(ColumnValidator columnValidator) {
        this.columnValidator = columnValidator;
    }

    public Pageable createPageable(int page, int size, List<String> sortByList, List<String> sortDirectionList, Class<?> domainClass) throws SortingArgumentException, InvalidColumnException {
        if (sortByList.size() != sortDirectionList.size()) {
            throw new SortingArgumentException();
        }

        for (String sortBy : sortByList) {
            if (!columnValidator.isValidColumn(domainClass, sortBy)) {
                throw new InvalidColumnException(sortBy + "라는 칼럼이 존재하지 않습니다.");
            }
        }

        for (String direction : sortDirectionList) {
            if (!isValidateSortDirection(direction)) {
                throw new SortingArgumentException(direction + "은 지원하지 않는 정렬 방식입니다.");
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
