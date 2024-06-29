package page.clab.api.domain.comment.application;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.comment.dto.response.CommentResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface FetchCommentsService {
    PagedResponseDto<CommentResponseDto> execute(Long boardId, Pageable pageable);
}