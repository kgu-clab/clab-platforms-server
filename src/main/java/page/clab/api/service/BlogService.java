package page.clab.api.service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.exception.NotFoundException;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.repository.BlogRepository;
import page.clab.api.type.dto.BlogDetailsResponseDto;
import page.clab.api.type.dto.BlogRequestDto;
import page.clab.api.type.dto.BlogResponseDto;
import page.clab.api.type.entity.Blog;
import page.clab.api.type.entity.Member;

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

    public List<BlogResponseDto> getBlogs(Pageable pageable) {
        Page<Blog> blogs = blogRepository.findAllByOrderByCreatedAtDesc(pageable);
        return blogs.map(BlogResponseDto::of).getContent();
    }

    public BlogDetailsResponseDto getBlogDetails(Long blogId) {
        Blog blog = getBlogByIdOrThrow(blogId);
        return BlogDetailsResponseDto.of(blog);
    }

    public List<BlogResponseDto> searchBlog(String keyword, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Blog> criteriaQuery = criteriaBuilder.createQuery(Blog.class);
        Root<Blog> root = criteriaQuery.from(Blog.class);
        List<Predicate> predicates = new ArrayList<>();
        if (keyword != null && !keyword.isEmpty()) {
            String keywordLowerCase = "%" + keyword.toLowerCase() + "%";
            predicates.add(criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), keywordLowerCase),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("subTitle")), keywordLowerCase),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("content")), keywordLowerCase),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("member").get("name")), keywordLowerCase)
            ));
        }
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[0]));
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createdAt")));
        TypedQuery<Blog> query = entityManager.createQuery(criteriaQuery);
        List<Blog> blogs = query.getResultList();
        Set<Blog> uniqueBlogs = new LinkedHashSet<>(blogs);
        List<Blog> distinctBlogs = new ArrayList<>(uniqueBlogs);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), uniqueBlogs.size());
        List<Blog> paginatedBlogs = new ArrayList<>(distinctBlogs.subList(start, end));
        return new PageImpl<>(paginatedBlogs, pageable, uniqueBlogs.size())
                .map(BlogResponseDto::of)
                .getContent();
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

    private Blog getBlogByIdOrThrow(Long blogId) {
        return blogRepository.findById(blogId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 게시글입니다."));
    }

}
