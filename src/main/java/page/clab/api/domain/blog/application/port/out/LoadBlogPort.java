package page.clab.api.domain.blog.application.port.out;

import page.clab.api.domain.blog.domain.Blog;

import java.util.Optional;

public interface LoadBlogPort {
    Optional<Blog> findById(Long blogId);
    Blog findByIdOrThrow(Long blogId);
}