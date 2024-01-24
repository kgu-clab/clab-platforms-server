package page.clab.api.domain.board.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.global.util.ModelMapperUtil;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardDetailsResponseDto {

    private Long id;

    private String nickname;

    private String title;

    private String content;

    private LocalDateTime createdAt;

    public static BoardDetailsResponseDto of(Board board) {
        BoardDetailsResponseDto boardResponseDto = ModelMapperUtil.getModelMapper().map(board, BoardDetailsResponseDto.class);
        boardResponseDto.setNickname(board.getNickName());
        return boardResponseDto;
    }

}
