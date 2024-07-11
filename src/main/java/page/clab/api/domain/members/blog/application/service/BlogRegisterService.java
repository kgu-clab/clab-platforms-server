package page.clab.api.domain.members.blog.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.member.application.port.in.RetrieveMemberUseCase;
import page.clab.api.domain.members.blog.application.dto.request.BlogRequestDto;
import page.clab.api.domain.members.blog.application.port.in.RegisterBlogUseCase;
import page.clab.api.domain.members.blog.application.port.out.RegisterBlogPort;
import page.clab.api.domain.members.blog.domain.Blog;

@Service
@RequiredArgsConstructor
public class BlogRegisterService implements RegisterBlogUseCase {

    private final RetrieveMemberUseCase retrieveMemberUseCase;
    private final RegisterBlogPort registerBlogPort;

    @Transactional
    @Override
    public Long registerBlog(BlogRequestDto requestDto) {
        String currentMemberId = retrieveMemberUseCase.getCurrentMemberId();
        Blog blog = BlogRequestDto.toEntity(requestDto, currentMemberId);
        return registerBlogPort.save(blog).getId();
    }
}
