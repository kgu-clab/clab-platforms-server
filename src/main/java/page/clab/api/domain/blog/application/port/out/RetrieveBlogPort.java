package page.clab.api.domain.blog.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.blog.domain.Blog;

import java.util.Optional;

public interface RetrieveBlogPort {
    Optional<Blog> findById(Long blogId);

    Blog findByIdOrThrow(Long blogId);

    Page<Blog> findByConditions(String title, String memberName, Pageable pageable);

    Page<Blog> findAllByIsDeletedTrue(Pageable pageable);
}
