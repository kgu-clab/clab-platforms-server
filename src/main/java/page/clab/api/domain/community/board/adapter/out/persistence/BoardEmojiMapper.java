package page.clab.api.domain.community.board.adapter.out.persistence;

import org.springframework.stereotype.Component;
import page.clab.api.domain.community.board.domain.BoardEmoji;

@Component
public class BoardEmojiMapper {

    public BoardEmojiJpaEntity toJpaEntity(BoardEmoji boardEmoji) {
        return BoardEmojiJpaEntity.builder()
                .id(boardEmoji.getId())
                .memberId(boardEmoji.getMemberId())
                .boardId(boardEmoji.getBoardId())
                .emoji(boardEmoji.getEmoji())
                .deletedAt(boardEmoji.getDeletedAt())
                .isDeleted(boardEmoji.isDeleted())
                .build();
    }

    public BoardEmoji toDomain(BoardEmojiJpaEntity entity) {
        return BoardEmoji.builder()
                .id(entity.getId())
                .memberId(entity.getMemberId())
                .boardId(entity.getBoardId())
                .emoji(entity.getEmoji())
                .deletedAt(entity.getDeletedAt())
                .isDeleted(entity.isDeleted())
                .build();
    }
}
