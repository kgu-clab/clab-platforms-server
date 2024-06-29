package page.clab.api.domain.board.application;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.board.domain.BoardCategory;
import page.clab.api.domain.board.dto.response.BoardCategoryResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface BoardsByCategoryRetrievalService {
    PagedResponseDto<BoardCategoryResponseDto> retrieve(BoardCategory category, Pageable pageable);
}
