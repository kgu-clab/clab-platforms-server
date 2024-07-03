package page.clab.api.domain.blog.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.blog.application.port.in.UpdateBlogUseCase;
import page.clab.api.domain.blog.application.port.out.LoadBlogPort;
import page.clab.api.domain.blog.application.port.out.RegisterBlogPort;
import page.clab.api.domain.blog.domain.Blog;
import page.clab.api.domain.blog.dto.request.BlogUpdateRequestDto;
import page.clab.api.domain.member.application.port.in.RetrieveMemberUseCase;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.global.exception.PermissionDeniedException;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class BlogUpdateService implements UpdateBlogUseCase {

    private final RetrieveMemberUseCase retrieveMemberUseCase;
    private final ValidationService validationService;
    private final LoadBlogPort loadBlogPort;
    private final RegisterBlogPort registerBlogPort;

    @Transactional
    @Override
    public Long update(Long blogId, BlogUpdateRequestDto requestDto) throws PermissionDeniedException {
        Member currentMember = retrieveMemberUseCase.getCurrentMember();
        Blog blog = loadBlogPort.findByIdOrThrow(blogId);
        blog.validateAccessPermission(currentMember);
        blog.update(requestDto);
        validationService.checkValid(blog);
        return registerBlogPort.save(blog).getId();
    }
}
