package page.clab.api.domain.board.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.board.domain.BoardLike;
import page.clab.api.domain.member.domain.Member;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {

    BoardLike findByBoardIdAndMemberId(Long boardId, String memberId);

    boolean existsByBoardIdAndMemberId(Long boardId, String memberId);

}
