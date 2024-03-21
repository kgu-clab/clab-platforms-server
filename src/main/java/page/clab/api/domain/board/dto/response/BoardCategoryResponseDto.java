package page.clab.api.domain.board.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.global.util.ModelMapperUtil;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardCategoryResponseDto {

    private Long id;

    private String category;

    private String writer;

    private String title;

    private LocalDateTime createdAt;

    public static BoardCategoryResponseDto of(Board board) {
        BoardCategoryResponseDto boardCategoryResponseDto = ModelMapperUtil.getModelMapper().map(board, BoardCategoryResponseDto.class);

        if(board.isWantAnonymous()){
            boardCategoryResponseDto.setWriter(board.getNickname());
        }
        else{
            boardCategoryResponseDto.setWriter(board.getMember().getName());
        }

        return boardCategoryResponseDto;
    }

}
