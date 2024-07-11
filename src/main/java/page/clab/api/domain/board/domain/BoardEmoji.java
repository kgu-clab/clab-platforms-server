package page.clab.api.domain.board.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardEmoji {

    private Long id;
    private String memberId;
    private Long boardId;
    private String emoji;
    private LocalDateTime deletedAt;
    private boolean isDeleted;

    public static BoardEmoji create(String memberId, Long boardId, String emoji) {
        return BoardEmoji.builder()
                .memberId(memberId)
                .boardId(boardId)
                .emoji(emoji)
                .build();
    }

    public void toggleIsDeletedStatus() {
        this.isDeleted = !this.isDeleted;
        this.deletedAt = this.isDeleted ? LocalDateTime.now() : null;
    }
}
