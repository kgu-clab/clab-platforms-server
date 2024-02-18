package page.clab.api.global.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class PagedResponseDto<T> {

    private final int currentPage;

    private final boolean hasPrevious;

    private final boolean hasNext;

    private final int totalPages;

    private final long totalItems;

    private final int take;

    private final List<T> items;

    public PagedResponseDto(Page<T> page) {
        this.currentPage = page.getNumber();
        this.hasPrevious = page.hasPrevious();
        this.hasNext = page.hasNext();
        this.totalPages = page.getTotalPages();
        this.totalItems = page.getTotalElements();
        this.take = page.getNumberOfElements();
        this.items = page.getContent();
    }

    public PagedResponseDto(List<T> ts, Pageable pageable, int size) {
        this.currentPage = pageable.getPageNumber();
        this.hasPrevious = pageable.getPageNumber() > 0;
        this.hasNext = ts.size() == size;
        this.totalPages = ts.size() / size;
        this.totalItems = ts.size();
        this.take = size;
        this.items = ts;
    }

}