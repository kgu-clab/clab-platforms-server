package page.clab.api.domain.blog.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.blog.domain.Blog;

public interface BlogRepositoryCustom {
    Page<Blog> findByConditions(String title, String memberName, Pageable pageable);
}
