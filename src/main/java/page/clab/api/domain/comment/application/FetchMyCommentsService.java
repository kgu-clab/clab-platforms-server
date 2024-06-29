package page.clab.api.domain.comment.application;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.comment.dto.response.CommentMyResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface FetchMyCommentsService {
    PagedResponseDto<CommentMyResponseDto> execute(Pageable pageable);
}