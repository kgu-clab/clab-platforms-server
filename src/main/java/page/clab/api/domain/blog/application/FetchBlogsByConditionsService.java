package page.clab.api.domain.blog.application;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.blog.dto.response.BlogResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface FetchBlogsByConditionsService {
    PagedResponseDto<BlogResponseDto> execute(String title, String memberName, Pageable pageable);
}
