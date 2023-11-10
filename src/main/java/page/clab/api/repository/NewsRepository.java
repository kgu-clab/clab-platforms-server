package page.clab.api.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.type.entity.News;

@Repository
public interface NewsRepository  extends JpaRepository<News, Long> {

    List<News> findByCategoryContaining(String category);

    List<News> findByTitleContaining(String title);
    
}
