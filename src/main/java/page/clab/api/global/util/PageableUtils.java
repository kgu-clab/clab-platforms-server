package page.clab.api.global.util;

import org.hibernate.annotations.NotFound;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import page.clab.api.global.exception.NotFoundException;

@Component
public class PageableUtils {

    private static ColumnValidator columnValidator;

    public PageableUtils(ColumnValidator columnValidator) {
        PageableUtils.columnValidator = columnValidator;
    }

    public static Pageable createPageable(int page, int size, String sortBy, String sortDirection, Class<?> entityClass) {
        if (!columnValidator.isValidColumn(entityClass, sortBy)) {
            throw new NotFoundException(sortBy + "라는 칼럼이 존재하지 않습니다.");
        }
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        return PageRequest.of(page, size, sort);
    }

}
