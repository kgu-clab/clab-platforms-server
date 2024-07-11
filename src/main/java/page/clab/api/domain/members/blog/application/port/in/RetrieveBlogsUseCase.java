package page.clab.api.domain.members.blog.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.members.blog.application.dto.response.BlogResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface RetrieveBlogsUseCase {
    PagedResponseDto<BlogResponseDto> retrieveBlogs(String title, String memberName, Pageable pageable);
}
