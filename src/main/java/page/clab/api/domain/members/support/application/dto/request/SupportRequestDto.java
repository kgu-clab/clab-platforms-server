package page.clab.api.domain.members.support.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import page.clab.api.domain.members.support.domain.SupportCategory;

import java.util.List;


@Getter
@Setter
public class SupportRequestDto {

    @NotNull(message = "{notNull.support.title}")
    @Schema(description = "제목", example = "계정 인증 관련 문의 드립니다", required = true)
    private String title;

    @NotNull(message = "{notNull.support.content}")
    @Schema(description = "내용", example = "얼마전 clab auth를 활용하여...", required = true)
    private String content;

    @Schema(description = "첨부파일 경로 리스트", example = "[\"/resources/files/boards/339609571877700_4305d83e-090a-480b-a470-b5e96164d113.png\", \"/resources/files/boards/4305d83e-090a-480b-a470-b5e96164d114.png\"]")
    private List<String> fileUrlList;

    @NotNull(message = "{notNull.support.type}")
    @Schema(description = "카테고리", example = "BUG", required = true)
    private SupportCategory category;

    @NotNull(message = "{notNull.support.wantAnonymous}")
    @Schema(description = "익명 사용 여부", example = "false", required = true)
    private boolean wantAnonymous;
}
