package page.clab.api.type.dto;

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

    private String category;

    private String title;

    private String content;

    private String writer;

    private String updateTime;

    private String createdAt;

    public static BoardDto of(Board board) {
        return ModelMapperUtil.getModelMapper().map(board, BoardDto.class);
    }
}
