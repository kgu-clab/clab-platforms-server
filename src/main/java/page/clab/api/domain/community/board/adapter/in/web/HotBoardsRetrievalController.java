package page.clab.api.domain.community.board.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.community.board.application.dto.response.BoardListResponseDto;
import page.clab.api.domain.community.board.application.port.in.RetrieveHotBoardsUseCase;
import page.clab.api.global.common.dto.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/boards")
@RequiredArgsConstructor
@Tag(name = "Community - Board", description = "커뮤니티 게시판")
public class HotBoardsRetrievalController {

    private final RetrieveHotBoardsUseCase retrieveHotBoardsUseCase;

    @Operation(summary = "[G] 커뮤니티 핫 게시글 목록 조회", description = "ROLE_GUEST 이상의 권한이 필요함<br>" +
            "반응(이모지), 댓글 수를 합친 결과가 높은 순으로 size만큼 조회 가능")
    @PreAuthorize("hasRole('GUEST')")
    @GetMapping("/hot")
    public ApiResponse<List<BoardListResponseDto>> retrieveHotBoards(
            @RequestParam(defaultValue = "5") int size
    ) {
        List<BoardListResponseDto> boards = retrieveHotBoardsUseCase.retrieveHotBoards(size);
        return ApiResponse.success(boards);
    }
}
