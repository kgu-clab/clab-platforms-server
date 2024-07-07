package page.clab.api.domain.comment.dao;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.comment.domain.CommentLike;

import java.util.Optional;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<CommentLike> findByCommentIdAndMemberId(Long commentId, String memberId);
}
