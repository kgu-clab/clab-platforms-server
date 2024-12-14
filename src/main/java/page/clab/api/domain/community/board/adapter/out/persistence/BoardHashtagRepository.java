package page.clab.api.domain.community.board.adapter.out.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardHashtagRepository extends JpaRepository<BoardHashtagJpaEntity, Long> {

    List<BoardHashtagJpaEntity> findAllByBoardId(Long boardId);

    @Query(value = "SELECT b.* FROM board_hashtag b WHERE b.board_id = :boardId", nativeQuery = true)
    List<BoardHashtagJpaEntity> findAllIncludingDeletedByBoardId(Long boardId);
}
