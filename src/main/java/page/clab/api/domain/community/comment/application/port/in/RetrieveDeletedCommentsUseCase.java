package page.clab.api.domain.community.comment.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.community.comment.application.dto.response.DeletedCommentResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface RetrieveDeletedCommentsUseCase {
    PagedResponseDto<DeletedCommentResponseDto> retrieveDeletedComments(Long boardId, Pageable pageable);
}
