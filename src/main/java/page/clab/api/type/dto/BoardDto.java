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
public class BoardDto {

    @NotNull
    private String category;

    @NotNull
    @Size(min = 1, max = 100)
    private String title;

    @NotNull
    private String content;

    @NotNull
    private String writer;

    private String updateTime;

    private String createdAt;

    public static BoardDto of(Board board) {
        return ModelMapperUtil.getModelMapper().map(board, BoardDto.class);
    }
}
