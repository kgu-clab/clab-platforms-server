package page.clab.api.domain.blog.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.blog.application.port.in.RemoveBlogUseCase;
import page.clab.api.domain.blog.application.port.out.LoadBlogPort;
import page.clab.api.domain.blog.application.port.out.RegisterBlogPort;
import page.clab.api.domain.blog.domain.Blog;
import page.clab.api.domain.member.application.port.in.RetrieveMemberUseCase;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.global.exception.PermissionDeniedException;

@Service
@RequiredArgsConstructor
public class BlogRemoveService implements RemoveBlogUseCase {

    private final RetrieveMemberUseCase retrieveMemberUseCase;
    private final LoadBlogPort loadBlogPort;
    private final RegisterBlogPort registerBlogPort;

    @Transactional
    @Override
    public Long remove(Long blogId) throws PermissionDeniedException {
        Member currentMember = retrieveMemberUseCase.getCurrentMember();
        Blog blog = loadBlogPort.findByIdOrThrow(blogId);
        blog.validateAccessPermission(currentMember);
        blog.delete();
        return registerBlogPort.save(blog).getId();
    }
}
