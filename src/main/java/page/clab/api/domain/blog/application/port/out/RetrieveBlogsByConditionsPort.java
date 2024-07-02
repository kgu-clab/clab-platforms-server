package page.clab.api.domain.blog.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.blog.domain.Blog;

public interface RetrieveBlogsByConditionsPort {
    Page<Blog> findByConditions(String title, String memberName, Pageable pageable);
}
