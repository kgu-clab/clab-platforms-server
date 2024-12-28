package page.clab.api.domain.community.board.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.community.board.application.dto.request.BoardUpdateRequestDto;
import page.clab.api.domain.community.board.application.port.in.UpdateBoardUseCase;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.exception.PermissionDeniedException;

@RestController
@RequestMapping("/api/v1/boards")
@RequiredArgsConstructor
@Tag(name = "Community - Board", description = "커뮤니티 게시판")
public class BoardUpdateController {

    private final UpdateBoardUseCase updateBoardUseCase;

    @Operation(summary = "[U] 커뮤니티 게시글 수정", description = "ROLE_USER 이상의 권한이 필요함")
    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/{boardId}")
    public ApiResponse<String> updateBoard(
        @PathVariable(name = "boardId") Long boardId,
        @Valid @RequestBody BoardUpdateRequestDto requestDto
    ) throws PermissionDeniedException {
        String id = updateBoardUseCase.updateBoard(boardId, requestDto);
        return ApiResponse.success(id);
    }
}
