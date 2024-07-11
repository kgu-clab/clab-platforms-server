package page.clab.api.domain.members.blog.application.port.out;

import page.clab.api.domain.members.blog.domain.Blog;

public interface RemoveBlogPort {
    void delete(Blog blog);
}
