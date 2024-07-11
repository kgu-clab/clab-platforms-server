package page.clab.api.domain.board.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.board.adapter.out.persistence.BoardJpaEntity;
import page.clab.api.domain.board.application.dto.response.BoardListResponseDto;
import page.clab.api.domain.board.application.port.in.RetrieveBoardsUseCase;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.InvalidColumnException;
import page.clab.api.global.exception.SortingArgumentException;
import page.clab.api.global.util.PageableUtils;

import java.util.List;

@RestController
@RequestMapping("/api/v1/boards")
@RequiredArgsConstructor
@Tag(name = "Board", description = "커뮤니티 게시판")
public class BoardsRetrievalController {

    private final RetrieveBoardsUseCase retrieveBoardsUseCase;

    @GetMapping("")
    @Operation(summary = "[U] 커뮤니티 게시글 목록 조회", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "페이지네이션 정렬에 사용할 수 있는 칼럼 : createdAt, id, updatedAt, memberId")
    @Secured({ "ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER" })
    public ApiResponse<PagedResponseDto<BoardListResponseDto>> retrieveBoards(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "sortBy", defaultValue = "createdAt") List<String> sortBy,
            @RequestParam(name = "sortDirection", defaultValue = "desc") List<String> sortDirection
    ) throws SortingArgumentException, InvalidColumnException {
        Pageable pageable = PageableUtils.createPageable(page, size, sortBy, sortDirection, BoardJpaEntity.class);
        PagedResponseDto<BoardListResponseDto> boards = retrieveBoardsUseCase.retrieveBoards(pageable);
        return ApiResponse.success(boards);
    }
}
