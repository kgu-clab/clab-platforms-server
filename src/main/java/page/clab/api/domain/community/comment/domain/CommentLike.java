package page.clab.api.domain.community.comment.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentLike {

    private Long commentLikeId;
    private String memberId;
    private Long commentId;

    public static CommentLike create(String memberId, Long commentId) {
        return CommentLike.builder()
            .memberId(memberId)
            .commentId(commentId)
            .build();
    }
}
