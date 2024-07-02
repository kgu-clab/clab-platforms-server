package page.clab.api.domain.blog.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.blog.dto.response.BlogResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface BlogsRetrievalUseCase {
    PagedResponseDto<BlogResponseDto> retrieve(String title, String memberName, Pageable pageable);
}
