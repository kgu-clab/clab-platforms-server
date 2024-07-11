package page.clab.api.domain.blog.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BlogRepositoryCustom {
    Page<BlogJpaEntity> findByConditions(String title, String memberName, Pageable pageable);
}
