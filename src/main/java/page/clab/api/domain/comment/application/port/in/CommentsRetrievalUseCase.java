package page.clab.api.domain.comment.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.comment.dto.response.CommentResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface CommentsRetrievalUseCase {
    PagedResponseDto<CommentResponseDto> retrieve(Long boardId, Pageable pageable);
}