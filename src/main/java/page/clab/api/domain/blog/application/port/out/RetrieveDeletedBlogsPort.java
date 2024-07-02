package page.clab.api.domain.blog.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.blog.domain.Blog;

public interface RetrieveDeletedBlogsPort {
    Page<Blog> findAllByIsDeletedTrue(Pageable pageable);
}
