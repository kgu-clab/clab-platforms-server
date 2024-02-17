package page.clab.api.domain.board.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Size;
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
public class BoardUpdateRequestDto {

    @Size(min = 1, max = 50, message = "{size.board.category}")
    @Schema(description = "카테고리", example = "공지사항")
    private String category;

    @Size(min = 1, max = 100, message = "{size.board.title}")
    @Schema(description = "제목", example = "2023년 2학기 모집 안내")
    private String title;

    @Size(min = 1, max = 10000, message = "{size.board.content}")
    @Schema(description = "내용", example = "2023년 2학기 모집 안내")
    private String content;

    @Column(name = "want_anonymous", nullable = false)
    @Schema(description = "익명 사용 여부", example = "false")
    private boolean wantAnonymous;

    public static BoardUpdateRequestDto of(Board board) {
        return ModelMapperUtil.getModelMapper().map(board, BoardUpdateRequestDto.class);
    }

}
