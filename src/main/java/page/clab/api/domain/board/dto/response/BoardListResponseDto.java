package page.clab.api.domain.board.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.board.domain.Board;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardListResponseDto {

    private Long id;

    private String writerId;

    private String writerName;

    private String category;

    private String title;

    private String content;

    private Long commentCount;

    private LocalDateTime createdAt;

    public static BoardListResponseDto toDto(Board board, Long commentCount) {
        return BoardListResponseDto.builder()
                .id(board.getId())
                .writerId(board.isWantAnonymous() ? null : board.getMember().getId())
                .writerName(board.isWantAnonymous() ? board.getNickname() : board.getMember().getName())
                .category(board.getCategory())
                .title(board.getTitle())
                .content(board.getContent())
                .commentCount(commentCount)
                .createdAt(board.getCreatedAt())
                .build();
    }

}
