package page.clab.api.domain.board.dao;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import page.clab.api.domain.board.domain.BoardLike;

import java.util.Optional;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<BoardLike> findByBoardIdAndMemberId(Long boardId, String memberId);

    boolean existsByBoardIdAndMemberId(Long boardId, String memberId);

}
