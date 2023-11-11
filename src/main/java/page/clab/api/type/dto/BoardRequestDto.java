package page.clab.api.type.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.type.entity.Board;
import page.clab.api.util.ModelMapperUtil;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardRequestDto {

    @NotNull(message = "{notNull.board.category}")
    @Size(min = 1, max = 50, message = "{size.board.category}")
    private String category;

    @NotNull(message = "{notNull.board.title}")
    @Size(min = 1, max = 100, message = "{size.board.title}")
    private String title;

    @NotNull(message = "{notNull.board.content}")
    private String content;

    public static BoardRequestDto of(Board board) {
        return ModelMapperUtil.getModelMapper().map(board, BoardRequestDto.class);
    }

}
