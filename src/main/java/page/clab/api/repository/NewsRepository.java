package page.clab.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.type.entity.News;

import java.util.List;

@Repository
public interface NewsRepository  extends JpaRepository<News, Long> {

    List<News> findAllByTitle(String title);

    List<News> findAllByCategory(String category);
}
