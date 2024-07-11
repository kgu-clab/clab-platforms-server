package page.clab.api.domain.members.blog.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import page.clab.api.domain.members.blog.application.port.out.RegisterBlogPort;
import page.clab.api.domain.members.blog.application.port.out.RemoveBlogPort;
import page.clab.api.domain.members.blog.application.port.out.RetrieveBlogPort;
import page.clab.api.domain.members.blog.domain.Blog;
import page.clab.api.global.exception.NotFoundException;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BlogPersistenceAdapter implements
        RegisterBlogPort,
        RetrieveBlogPort,
        RemoveBlogPort {

    private final BlogRepository blogRepository;
    private final BlogMapper blogMapper;

    @Override
    public Blog save(Blog blog) {
        BlogJpaEntity entity = blogMapper.toJpaEntity(blog);
        BlogJpaEntity savedEntity = blogRepository.save(entity);
        return blogMapper.toDomain(savedEntity);
    }

    @Override
    public void delete(Blog blog) {
        BlogJpaEntity entity = blogMapper.toJpaEntity(blog);
        blogRepository.delete(entity);
    }

    @Override
    public Optional<Blog> findById(Long blogId) {
        return blogRepository.findById(blogId)
                .map(blogMapper::toDomain);
    }

    @Override
    public Blog findByIdOrThrow(Long blogId) {
        return blogRepository.findById(blogId)
                .map(blogMapper::toDomain)
                .orElseThrow(() -> new NotFoundException("[Blog] id: " + blogId + "에 해당하는 게시글이 존재하지 않습니다."));
    }

    @Override
    public Page<Blog> findByConditions(String title, String memberName, Pageable pageable) {
        return blogRepository.findByConditions(title, memberName, pageable)
                .map(blogMapper::toDomain);
    }

    @Override
    public Page<Blog> findAllByIsDeletedTrue(Pageable pageable) {
        return blogRepository.findAllByIsDeletedTrue(pageable)
                .map(blogMapper::toDomain);
    }
}
