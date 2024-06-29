package page.clab.api.domain.board.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import page.clab.api.global.common.domain.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE board SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
@Table(name = "board_emoji", indexes = {
        @Index(name = "idx_board_emoji_board_id_emoji", columnList = "board_id, emoji"),
        @Index(name = "idx_board_emoji_board_id_emoji_member_id", columnList = "board_id, emoji, member_id")
})
public class BoardEmoji extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private String memberId;

    @Column(name = "board_id", nullable = false)
    private Long boardId;

    @Column(name = "emoji", nullable = false)
    private String emoji;

    private LocalDateTime deletedAt;

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
