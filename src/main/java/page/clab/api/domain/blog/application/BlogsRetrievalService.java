package page.clab.api.domain.blog.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.blog.application.port.in.BlogsRetrievalUseCase;
import page.clab.api.domain.blog.application.port.out.RetrieveBlogsByConditionsPort;
import page.clab.api.domain.blog.domain.Blog;
import page.clab.api.domain.blog.dto.response.BlogResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class BlogsRetrievalService implements BlogsRetrievalUseCase {

    private final RetrieveBlogsByConditionsPort retrieveBlogsByConditionsPort;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<BlogResponseDto> retrieve(String title, String memberName, Pageable pageable) {
        Page<Blog> blogs = retrieveBlogsByConditionsPort.findByConditions(title, memberName, pageable);
        return new PagedResponseDto<>(blogs.map(BlogResponseDto::toDto));
    }
}
