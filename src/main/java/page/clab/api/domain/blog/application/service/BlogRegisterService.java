package page.clab.api.domain.blog.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.blog.application.port.in.RegisterBlogUseCase;
import page.clab.api.domain.blog.application.port.out.RegisterBlogPort;
import page.clab.api.domain.blog.domain.Blog;
import page.clab.api.domain.blog.dto.request.BlogRequestDto;
import page.clab.api.domain.member.application.port.in.RetrieveMemberUseCase;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class BlogRegisterService implements RegisterBlogUseCase {

    private final RetrieveMemberUseCase retrieveMemberUseCase;
    private final ValidationService validationService;
    private final RegisterBlogPort registerBlogPort;

    @Transactional
    @Override
    public Long register(BlogRequestDto requestDto) {
        String currentMemberId = retrieveMemberUseCase.getCurrentMemberId();
        Blog blog = BlogRequestDto.toEntity(requestDto, currentMemberId);
        validationService.checkValid(blog);
        return registerBlogPort.save(blog).getId();
    }
}
