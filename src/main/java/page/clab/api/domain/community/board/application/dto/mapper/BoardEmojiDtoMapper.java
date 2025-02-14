package page.clab.api.domain.community.board.application.dto.mapper;

import org.springframework.stereotype.Component;
import page.clab.api.domain.community.board.application.dto.response.BoardEmojiToggleResponseDto;
import page.clab.api.domain.community.board.domain.BoardEmoji;

@Component
public class BoardEmojiDtoMapper {

    public BoardEmojiToggleResponseDto toDto(BoardEmoji boardEmoji, String category) {
        return BoardEmojiToggleResponseDto.builder()
                .boardId(boardEmoji.getBoardId())
                .category(category)
                .emoji(boardEmoji.getEmoji())
                .isDeleted(boardEmoji.getIsDeleted())
                .build();
    }
}
