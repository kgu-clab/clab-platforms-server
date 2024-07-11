package page.clab.api.domain.community.board.adapter.out.persistence;

import org.springframework.stereotype.Component;
import page.clab.api.domain.community.board.domain.Board;

@Component
public class BoardMapper {

    public BoardJpaEntity toJpaEntity(Board board) {
        return BoardJpaEntity.builder()
                .id(board.getId())
                .memberId(board.getMemberId())
                .nickname(board.getNickname())
                .category(board.getCategory())
                .title(board.getTitle())
                .content(board.getContent())
                .uploadedFiles(board.getUploadedFiles())
                .imageUrl(board.getImageUrl())
                .wantAnonymous(board.isWantAnonymous())
                .isDeleted(board.isDeleted())
                .build();
    }

    public Board toDomain(BoardJpaEntity entity) {
        return Board.builder()
                .id(entity.getId())
                .memberId(entity.getMemberId())
                .nickname(entity.getNickname())
                .category(entity.getCategory())
                .title(entity.getTitle())
                .content(entity.getContent())
                .uploadedFiles(entity.getUploadedFiles())
                .imageUrl(entity.getImageUrl())
                .wantAnonymous(entity.isWantAnonymous())
                .isDeleted(entity.isDeleted())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
