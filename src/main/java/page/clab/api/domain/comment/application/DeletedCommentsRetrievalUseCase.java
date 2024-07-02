package page.clab.api.domain.comment.application;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.comment.dto.response.DeletedCommentResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface DeletedCommentsRetrievalUseCase {
    PagedResponseDto<DeletedCommentResponseDto> retrieve(Long boardId, Pageable pageable);
}
