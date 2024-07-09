package page.clab.api.domain.board.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import page.clab.api.domain.board.domain.BoardCategory;

@Getter
@Setter
public class BoardUpdateRequestDto {

    @Schema(description = "카테고리", example = "NOTICE")
    private BoardCategory category;

    @Schema(description = "제목", example = "2023년 2학기 모집 안내")
    private String title;

    @Schema(description = "내용", example = "2023년 2학기 모집 안내")
    private String content;

    @Schema(description = "썸네일 이미지 URL", example = "/resources/files/boards/339609571877700_4305d83e-090a-480b-a470-b5e96164d113.png")
    private String imageUrl;

    @NotNull(message = "{notNull.board.wantAnonymous}")
    @Schema(description = "익명 사용 여부", example = "false")
    private boolean wantAnonymous;
}
