package page.clab.api.global.common.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

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

}