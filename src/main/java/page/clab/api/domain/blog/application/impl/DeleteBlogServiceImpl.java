package page.clab.api.domain.blog.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.blog.application.DeleteBlogService;
import page.clab.api.domain.blog.dao.BlogRepository;
import page.clab.api.domain.blog.domain.Blog;
import page.clab.api.domain.member.application.MemberLookupService;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.PermissionDeniedException;

@Service
@RequiredArgsConstructor
public class DeleteBlogServiceImpl implements DeleteBlogService {

    private final MemberLookupService memberLookupService;
    private final BlogRepository blogRepository;

    @Transactional
    @Override
    public Long execute(Long blogId) throws PermissionDeniedException {
        Member currentMember = memberLookupService.getCurrentMember();
        Blog blog = getBlogByIdOrThrow(blogId);
        blog.validateAccessPermission(currentMember);
        blog.delete();
        blogRepository.save(blog);
        return blog.getId();
    }

    private Blog getBlogByIdOrThrow(Long blogId) {
        return blogRepository.findById(blogId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 게시글입니다."));
    }
}