package page.clab.api.domain.community.comment.adapter.out.persistence;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLikeJpaEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<CommentLikeJpaEntity> findByCommentIdAndMemberId(Long commentId, String memberId);
}
