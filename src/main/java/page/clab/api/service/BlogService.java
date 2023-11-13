package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.exception.NotFoundException;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.repository.BlogRepository;
import page.clab.api.type.dto.BlogRequestDto;
import page.clab.api.type.dto.BlogResponseDto;
import page.clab.api.type.entity.Blog;
import page.clab.api.type.entity.Member;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class BlogService {

    private final MemberService memberService;

    private final BlogRepository blogRepository;

    private final EntityManager entityManager;

    public void createBlog(BlogRequestDto blogRequestDto) {
        Member member = memberService.getCurrentMember();
        Blog blog = Blog.of(blogRequestDto);
        blog.setMember(member);
        blogRepository.save(blog);
    }

    public List<BlogResponseDto> getBlogs() {
        List<Blog> blogs = blogRepository.findAll();
        return blogs.stream()
                .map(BlogResponseDto::of)
                .collect(Collectors.toList());
    }

    public List<BlogResponseDto> searchBlog(String keyword) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Blog> criteriaQuery = criteriaBuilder.createQuery(Blog.class);
        Root<Blog> root = criteriaQuery.from(Blog.class);
        Predicate predicate = criteriaBuilder.or(
                criteriaBuilder.like(root.get("title"), "%" + keyword + "%"),
                criteriaBuilder.like(root.get("subTitle"), "%" + keyword + "%"),
                criteriaBuilder.like(root.get("content"), "%" + keyword + "%"),
                criteriaBuilder.like(root.get("tag"), "%" + keyword + "%"),
                criteriaBuilder.like(root.get("member").get("name"), "%" + keyword + "%")
        );
        criteriaQuery.select(root).where(predicate);
        List<Blog> blogs = entityManager.createQuery(criteriaQuery).getResultList();
        return blogs.stream()
                .map(BlogResponseDto::of)
                .collect(Collectors.toList());
    }

    public void updateBlog(Long blogId, BlogRequestDto blogRequestDto) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        Blog blog = getBlogByIdOrThrow(blogId);
        if (!blog.getMember().equals(member)) {
            throw new PermissionDeniedException("해당 게시글을 수정할 권한이 없습니다.");
        }
        Blog updatedBlog = Blog.of(blogRequestDto);
        updatedBlog.setId(blog.getId());
        updatedBlog.setMember(blog.getMember());
        updatedBlog.setUpdateTime(LocalDateTime.now());
        updatedBlog.setCreatedAt(blog.getCreatedAt());
        blogRepository.save(updatedBlog);
    }

    public void deleteBlog(Long blogId) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        Blog blog = getBlogByIdOrThrow(blogId);
        if (!(blog.getMember().equals(member) || memberService.isMemberAdminRole(member))) {
            throw new PermissionDeniedException("해당 게시글을 삭제할 권한이 없습니다.");
        }
        blogRepository.delete(blog);
    }

    public Blog getBlogByIdOrThrow(Long blogId) {
        return blogRepository.findById(blogId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 게시글입니다."));
    }

}
