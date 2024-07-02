package page.clab.api.domain.blog.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import page.clab.api.domain.blog.application.port.out.LoadBlogPort;
import page.clab.api.domain.blog.application.port.out.RegisterBlogPort;
import page.clab.api.domain.blog.application.port.out.RemoveBlogPort;
import page.clab.api.domain.blog.application.port.out.RetrieveBlogsByConditionsPort;
import page.clab.api.domain.blog.application.port.out.RetrieveDeletedBlogsPort;
import page.clab.api.domain.blog.domain.Blog;
import page.clab.api.global.exception.NotFoundException;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BlogPersistenceAdapter implements
        RegisterBlogPort,
        RemoveBlogPort,
        LoadBlogPort,
        RetrieveBlogsByConditionsPort,
        RetrieveDeletedBlogsPort {

    private final BlogRepository blogRepository;

    @Override
    public Blog save(Blog blog) {
        return blogRepository.save(blog);
    }

    @Override
    public void delete(Blog blog) {
        blogRepository.delete(blog);
    }

    @Override
    public Optional<Blog> findById(Long blogId) {
        return blogRepository.findById(blogId);
    }

    @Override
    public Blog findByIdOrThrow(Long blogId) {
        return blogRepository.findById(blogId)
                .orElseThrow(() -> new NotFoundException("[Blog] id: " + blogId + "에 해당하는 게시글이 존재하지 않습니다."));
    }

    @Override
    public Page<Blog> findByConditions(String title, String memberName, Pageable pageable) {
        return blogRepository.findByConditions(title, memberName, pageable);
    }

    @Override
    public Page<Blog> findAllByIsDeletedTrue(Pageable pageable) {
        return blogRepository.findAllByIsDeletedTrue(pageable);
    }
}
