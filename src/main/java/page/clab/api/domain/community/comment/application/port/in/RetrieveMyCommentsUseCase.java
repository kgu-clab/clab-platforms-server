package page.clab.api.domain.community.comment.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.community.comment.application.dto.response.CommentMyResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface RetrieveMyCommentsUseCase {

    PagedResponseDto<CommentMyResponseDto> retrieveMyComments(Pageable pageable);
}
