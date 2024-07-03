package page.clab.api.domain.blog.application.port.out;

import page.clab.api.domain.blog.domain.Blog;

public interface RegisterBlogPort {
    Blog save(Blog blog);
}
