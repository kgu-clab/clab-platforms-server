package page.clab.api.domain.community.board.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.community.board.application.dto.response.BoardListResponseDto;
import page.clab.api.domain.community.board.application.port.in.RetrieveHotBoardsUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/boards")
@RequiredArgsConstructor
@Tag(name = "Community - Board", description = "커뮤니티 게시판")
public class HotBoardsRetrievalController {

    private final RetrieveHotBoardsUseCase retrieveHotBoardsUseCase;

    @Operation(summary = "[G] 커뮤니티 인기 게시글 목록 조회", description = "ROLE_GUEST 이상의 권한이 필요함<br>" +
            "인기게시글 선정 전략별 조회가 가능함<br>" +
            "- DEFAULT : 반응 순 기본 전략<br>")
    @PreAuthorize("hasRole('GUEST')")
    @GetMapping("/hot")
    public ApiResponse<List<BoardListResponseDto>> retrieveHotBoards(
            @RequestParam(name = "strategyName", defaultValue = "DEFAULT") String strategyName
    ) {
        List<BoardListResponseDto> boards = retrieveHotBoardsUseCase.retrieveHotBoards(strategyName);
        return ApiResponse.success(boards);
    }
}
