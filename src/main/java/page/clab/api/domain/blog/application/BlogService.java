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
import page.clab.api.domain.member.application.MemberLookupService;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.PermissionDeniedException;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class BlogService {

    private final MemberLookupService memberLookupService;

    private final ValidationService validationService;

    private final BlogRepository blogRepository;

    @Transactional
    public Long createBlog(BlogRequestDto requestDto) {
        String currentMemberId = memberLookupService.getCurrentMemberId();
        Blog blog = BlogRequestDto.toEntity(requestDto, currentMemberId);
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
        Member currentMember = memberLookupService.getCurrentMember();
        Blog blog = getBlogByIdOrThrow(blogId);
        boolean isOwner = blog.isOwner(currentMember);
        return BlogDetailsResponseDto.toDto(blog, currentMember.getId(), currentMember.getName(), isOwner);
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<BlogDetailsResponseDto> getDeletedBlogs(Pageable pageable) {
        Member currentMember = memberLookupService.getCurrentMember();
        Page<Blog> blogs = blogRepository.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(blogs.map(blog -> BlogDetailsResponseDto.toDto(blog, currentMember.getId(), currentMember.getName(), blog.isOwner(currentMember))));
    }

    @Transactional
    public Long updateBlog(Long blogId, BlogUpdateRequestDto requestDto) throws PermissionDeniedException {
        Member currentMember = memberLookupService.getCurrentMember();
        Blog blog = getBlogByIdOrThrow(blogId);
        blog.validateAccessPermission(currentMember);
        blog.update(requestDto);
        validationService.checkValid(blog);
        return blogRepository.save(blog).getId();
    }

    @Transactional
    public Long deleteBlog(Long blogId) throws PermissionDeniedException {
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
