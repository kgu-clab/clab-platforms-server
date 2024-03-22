package page.clab.api.domain.board.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardUpdateRequestDto {

    @Schema(description = "카테고리", example = "공지사항")
    private String category;

    @Schema(description = "제목", example = "2023년 2학기 모집 안내")
    private String title;

    @Schema(description = "내용", example = "2023년 2학기 모집 안내")
    private String content;

    @NotNull(message = "{notNull.board.wantAnonymous}")
    @Schema(description = "익명 사용 여부", example = "false")
    private boolean wantAnonymous;

}
