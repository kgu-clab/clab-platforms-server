package page.clab.api.domain.blog.application.port.out;

import page.clab.api.domain.blog.domain.Blog;

public interface RemoveBlogPort {
    void delete(Blog blog);
}
