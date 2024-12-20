package page.clab.api.domain.community.board.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.community.board.application.dto.response.BoardEmojiToggleResponseDto;
import page.clab.api.domain.community.board.application.port.in.ToggleBoardEmojiUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/boards")
@RequiredArgsConstructor
@Tag(name = "Community - Board", description = "커뮤니티 게시판")
public class BoardEmojiToggleController {

    private final ToggleBoardEmojiUseCase toggleBoardEmojiUseCase;

    @Operation(summary = "[U] 커뮤니티 게시글 이모지 누르기/취소하기", description = "ROLE_USER 이상의 권한이 필요함")
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{boardId}/react/{emoji}")
    public ApiResponse<BoardEmojiToggleResponseDto> toggleEmojiStatus(
            @PathVariable(name = "boardId") Long boardId,
            @PathVariable(name = "emoji") String emoji
    ) {
        BoardEmojiToggleResponseDto responseDto = toggleBoardEmojiUseCase.toggleEmojiStatus(boardId, emoji);
        return ApiResponse.success(responseDto);
    }
}
