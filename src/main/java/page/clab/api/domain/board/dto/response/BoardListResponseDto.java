package page.clab.api.domain.board.dto.response;

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
public class BoardListResponseDto {

    private Long id;

    private String category;

    private String title;

    public static BoardListResponseDto of(Board board) {
        BoardListResponseDto boardResponseDto = ModelMapperUtil.getModelMapper().map(board, BoardListResponseDto.class);
        return boardResponseDto;
    }

}
