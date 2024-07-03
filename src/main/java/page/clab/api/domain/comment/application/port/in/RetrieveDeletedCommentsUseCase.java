package page.clab.api.domain.comment.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.comment.dto.response.DeletedCommentResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface RetrieveDeletedCommentsUseCase {
    PagedResponseDto<DeletedCommentResponseDto> retrieve(Long boardId, Pageable pageable);
}
