package page.clab.api.domain.board.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.board.application.port.in.ToggleBoardEmojiUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/boards")
@RequiredArgsConstructor
@Tag(name = "Board", description = "커뮤니티 게시판")
public class BoardEmojiToggleController {

    private final ToggleBoardEmojiUseCase toggleBoardEmojiUseCase;

    @PostMapping("/{boardId}/react/{emoji}")
    @Operation(summary = "[U] 커뮤니티 게시글 이모지 누르기/취소하기", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    public ApiResponse<String> toggleEmojiStatus(
            @PathVariable(name = "boardId") Long boardId,
            @PathVariable(name = "emoji") String emoji
    ) {
        String id = toggleBoardEmojiUseCase.toggleEmojiStatus(boardId, emoji);
        return ApiResponse.success(id);
    }
}