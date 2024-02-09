package page.clab.api.domain.comment.dao;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.board.domain.BoardLike;
import page.clab.api.domain.comment.domain.Comment;
import page.clab.api.domain.comment.domain.CommentLike;
import page.clab.api.domain.member.domain.Member;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    CommentLike findByCommentIdAndMemberId(Long commentId, String memberId);

    boolean existsByCommentIdAndMemberId(Long commentId, String memberId);

}
