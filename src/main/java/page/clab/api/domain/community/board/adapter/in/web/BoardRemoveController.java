package page.clab.api.domain.community.board.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.community.board.application.port.in.RemoveBoardUseCase;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.exception.PermissionDeniedException;

@RestController
@RequestMapping("/api/v1/boards")
@RequiredArgsConstructor
@Tag(name = "Community - Board", description = "커뮤니티 게시판")
public class BoardRemoveController {

    private final RemoveBoardUseCase removeBoardUseCase;

    @Operation(summary = "[U] 커뮤니티 게시글 삭제", description = "ROLE_USER 이상의 권한이 필요함")
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{boardId}")
    public ApiResponse<String> removeBoard(
            @PathVariable(name = "boardId") Long boardId
    ) throws PermissionDeniedException {
        String id = removeBoardUseCase.removeBoard(boardId);
        return ApiResponse.success(id);
    }
}
