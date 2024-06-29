package page.clab.api.domain.board.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.board.application.FetchBoardDetailsService;
import page.clab.api.domain.board.dto.response.BoardDetailsResponseDto;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/boards")
@RequiredArgsConstructor
@Tag(name = "Board", description = "커뮤니티 게시판")
public class FetchBoardDetailsController {

    private final FetchBoardDetailsService fetchBoardDetailsService;

    @GetMapping("/{boardId}")
    @Operation(summary = "[U] 커뮤니티 게시글 상세 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    public ApiResponse<BoardDetailsResponseDto> getBoardDetails(
            @PathVariable(name = "boardId") Long boardId
    ) {
        BoardDetailsResponseDto board = fetchBoardDetailsService.fetchBoardDetails(boardId);
        return ApiResponse.success(board);
    }
}
