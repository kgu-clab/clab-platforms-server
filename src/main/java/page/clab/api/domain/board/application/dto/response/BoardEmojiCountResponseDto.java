package page.clab.api.domain.board.application.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BoardEmojiCountResponseDto {

    private String emoji;

    private Long count;

    private Boolean isOwner;

    public BoardEmojiCountResponseDto(String emoji, Long count, Boolean isOwner) {
        this.emoji = emoji;
        this.count = count;
        this.isOwner = isOwner;
    }

    public static BoardEmojiCountResponseDto toDto(String emoji, Long count, Boolean isOwner) {
        return BoardEmojiCountResponseDto.builder()
                .emoji(emoji)
                .count(count)
                .isOwner(isOwner)
                .build();
    }
}
