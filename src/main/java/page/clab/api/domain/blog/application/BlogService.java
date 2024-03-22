package page.clab.api.domain.blog.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.blog.dao.BlogRepository;
import page.clab.api.domain.blog.domain.Blog;
import page.clab.api.domain.blog.dto.request.BlogRequestDto;
import page.clab.api.domain.blog.dto.request.BlogUpdateRequestDto;
import page.clab.api.domain.blog.dto.response.BlogDetailsResponseDto;
import page.clab.api.domain.blog.dto.response.BlogResponseDto;
import page.clab.api.domain.member.application.MemberService;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.PermissionDeniedException;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class BlogService {

    private final MemberService memberService;

    private final ValidationService validationService;

    private final BlogRepository blogRepository;

    public Long createBlog(BlogRequestDto requestDto) {
        Member currentMember = memberService.getCurrentMember();
        Blog blog = BlogRequestDto.toEntity(requestDto, currentMember);
        validationService.checkValid(blog);
        return blogRepository.save(blog).getId();
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<BlogResponseDto> getBlogsByConditions(String title, String memberName, Pageable pageable) {
        Page<Blog> blogs = blogRepository.findByConditions(title, memberName, pageable);
        return new PagedResponseDto<>(blogs.map(BlogResponseDto::toDto));
    }

    @Transactional(readOnly = true)
    public BlogDetailsResponseDto getBlogDetails(Long blogId) {
        Member currentMember = memberService.getCurrentMember();
        Blog blog = getBlogByIdOrThrow(blogId);
        boolean isOwner = blog.isOwner(currentMember);
        return BlogDetailsResponseDto.toDto(blog, isOwner);
    }

    @Transactional
    public Long updateBlog(Long blogId, BlogUpdateRequestDto requestDto) throws PermissionDeniedException {
        Member currentMember = memberService.getCurrentMember();
        Blog blog = getBlogByIdOrThrow(blogId);
        blog.validateAccessPermission(currentMember);
        blog.update(requestDto);
        validationService.checkValid(blog);
        return blogRepository.save(blog).getId();
    }

    public Long deleteBlog(Long blogId) throws PermissionDeniedException {
        Member currentMember = memberService.getCurrentMember();
        Blog blog = getBlogByIdOrThrow(blogId);
        blog.validateAccessPermission(currentMember);
        blogRepository.delete(blog);
        return blog.getId();
    }

    private Blog getBlogByIdOrThrow(Long blogId) {
        return blogRepository.findById(blogId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 게시글입니다."));
    }

}
