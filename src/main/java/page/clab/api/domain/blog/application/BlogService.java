package page.clab.api.domain.blog.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
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

@Service
@RequiredArgsConstructor
public class BlogService {

    private final MemberService memberService;

    private final BlogRepository blogRepository;

    public Long createBlog(BlogRequestDto blogRequestDto) {
        Member member = memberService.getCurrentMember();
        Blog blog = Blog.of(blogRequestDto);
        blog.setMember(member);
        return blogRepository.save(blog).getId();
    }

    public PagedResponseDto<BlogResponseDto> getBlogsByConditions(String title, String memberName, Pageable pageable) {
        Page<Blog> blogs = blogRepository.findByConditions(title, memberName, pageable);
        return new PagedResponseDto<>(blogs.map(BlogResponseDto::of));
    }

    public BlogDetailsResponseDto getBlogDetails(Long blogId) {
        Member member = memberService.getCurrentMember();
        Blog blog = getBlogByIdOrThrow(blogId);
        BlogDetailsResponseDto blogDetailsResponseDto = BlogDetailsResponseDto.of(blog);
        blogDetailsResponseDto.setOwner(isMemberBlogWriter(member,blog));
        return blogDetailsResponseDto;
    }

    public Long updateBlog(Long blogId, BlogUpdateRequestDto blogUpdateRequestDto) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        Blog blog = getBlogByIdOrThrow(blogId);
        if (!isMemberBlogWriter(member, blog)) {
            throw new PermissionDeniedException("해당 게시글을 수정할 권한이 없습니다.");
        }
        blog.update(blogUpdateRequestDto);
        return blogRepository.save(blog).getId();
    }

    public Long deleteBlog(Long blogId) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        Blog blog = getBlogByIdOrThrow(blogId);
        if (!(isMemberBlogWriter(member, blog) || memberService.isMemberAdminRole(member))) {
            throw new PermissionDeniedException("해당 게시글을 삭제할 권한이 없습니다.");
        }
        blogRepository.delete(blog);
        return blog.getId();
    }

    private Blog getBlogByIdOrThrow(Long blogId) {
        return blogRepository.findById(blogId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 게시글입니다."));
    }

    private boolean isMemberBlogWriter(Member member, Blog blog) {
        return blog.getMember().equals(member);
    }

}
