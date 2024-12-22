package page.clab.api.domain.community.board.domain;

import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
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
@Table(name = "board_hashtag", indexes = {
        @Index(name = "idx_board_hashtag_board_id", columnList = "boardId"),
        @Index(name = "idx_board_hashtag_hashtag_id", columnList = "hashtagId"),
})
public class BoardHashtag {

    Long id;
    Long boardId;
    Long hashtagId;
    Boolean isDeleted;

    public void toggleIsDeletedStatus() {
        this.isDeleted = !this.isDeleted;
    }
}
