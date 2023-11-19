package page.clab.api.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.type.entity.News;

@Repository
public interface NewsRepository  extends JpaRepository<News, Long> {

    Page<News> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<News> findByCategoryContainingOrderByCreatedAtDesc(String category, Pageable pageable);

    Page<News> findByTitleContainingOrderByCreatedAtDesc(String title, Pageable pageable);

}
