package page.clab.api.domain.board.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.board.domain.Board;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardListResponseDto {

    private Long id;

    private String category;

    private String title;

    public static BoardListResponseDto toDto(Board board) {
        return BoardListResponseDto.builder()
                .id(board.getId())
                .category(board.getCategory())
                .title(board.getTitle())
                .build();
    }

}
