package page.clab.api.domain.community.board.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.community.board.application.dto.response.BoardDetailsResponseDto;
import page.clab.api.domain.community.board.application.port.in.RetrieveBoardDetailsUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/boards")
@RequiredArgsConstructor
@Tag(name = "Community - Board", description = "커뮤니티 게시판")
public class BoardDetailsRetrievalController {

    private final RetrieveBoardDetailsUseCase retrieveBoardDetailsUseCase;

    @Operation(summary = "[U] 커뮤니티 게시글 상세 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{boardId}")
    public ApiResponse<BoardDetailsResponseDto> retrieveBoardDetails(
            @PathVariable(name = "boardId") Long boardId
    ) {
        BoardDetailsResponseDto board = retrieveBoardDetailsUseCase.retrieveBoardDetails(boardId);
        return ApiResponse.success(board);
    }
}
