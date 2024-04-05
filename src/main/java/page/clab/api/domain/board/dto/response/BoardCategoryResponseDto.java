package page.clab.api.domain.board.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.board.domain.BoardCategory;

import java.time.LocalDateTime;

@Getter
@Builder
public class BoardCategoryResponseDto {

    private Long id;

    private BoardCategory category;

    private String writerName;

    private String title;

    private LocalDateTime createdAt;

    public static BoardCategoryResponseDto toDto(Board board) {
        return BoardCategoryResponseDto.builder()
                .id(board.getId())
                .category(board.getCategory())
                .writerName(board.isWantAnonymous() ? board.getNickname() : board.getMember().getName())
                .title(board.getTitle())
                .createdAt(board.getCreatedAt())
                .build();
    }

}
