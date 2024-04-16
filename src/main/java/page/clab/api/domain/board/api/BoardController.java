package page.clab.api.domain.board.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.board.application.BoardService;
import page.clab.api.domain.board.domain.BoardCategory;
import page.clab.api.domain.board.dto.request.BoardRequestDto;
import page.clab.api.domain.board.dto.request.BoardUpdateRequestDto;
import page.clab.api.domain.board.dto.response.BoardCategoryResponseDto;
import page.clab.api.domain.board.dto.response.BoardDetailsResponseDto;
import page.clab.api.domain.board.dto.response.BoardListResponseDto;
import page.clab.api.domain.board.dto.response.BoardMyResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.dto.ResponseModel;
import page.clab.api.global.exception.PermissionDeniedException;

@RestController
@RequestMapping("/api/v1/boards")
@RequiredArgsConstructor
@Tag(name = "Board", description = "커뮤니티 게시판")
@Slf4j
public class BoardController {

    private final BoardService boardService;

    @Operation(summary = "[U] 커뮤니티 게시글 생성", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping("")
    public ResponseModel<Long> createBoard(
            @Valid @RequestBody BoardRequestDto requestDto
    ) throws PermissionDeniedException {
        Long id = boardService.createBoard(requestDto);
        return ResponseModel.success(id);
    }

    @GetMapping("")
    @Operation(summary = "[U] 커뮤니티 게시글 목록 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    public ResponseModel<PagedResponseDto<BoardListResponseDto>> getBoards(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<BoardListResponseDto> boards = boardService.getBoards(pageable);
        return ResponseModel.success(boards);
    }

    @GetMapping("/{boardId}")
    @Operation(summary = "[U] 커뮤니티 게시글 상세 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    public ResponseModel<BoardDetailsResponseDto> getBoardDetails(
            @PathVariable(name = "boardId") Long boardId
    ) {
        BoardDetailsResponseDto board = boardService.getBoardDetails(boardId);
        return ResponseModel.success(board);
    }

    @GetMapping("/my-boards")
    @Operation(summary = "[U] 내가 쓴 커뮤니티 게시글 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    public ResponseModel<PagedResponseDto<BoardMyResponseDto>> getMyBoards(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<BoardMyResponseDto> board = boardService.getMyBoards(pageable);
        return ResponseModel.success(board);
    }

    @GetMapping("/category")
    @Operation(summary = "[U] 커뮤니티 게시글 카테고리별 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    public ResponseModel<PagedResponseDto<BoardCategoryResponseDto>> getBoardsByCategory(
            @RequestParam(name = "category") BoardCategory category,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<BoardCategoryResponseDto> boards = boardService.getBoardsByCategory(category, pageable);
        return ResponseModel.success(boards);
    }

    @PatchMapping("/{boardId}")
    @Operation(summary = "[U] 커뮤니티 게시글 수정", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    public ResponseModel<Long> updateBoard(
            @PathVariable(name = "boardId") Long boardId,
            @Valid @RequestBody BoardUpdateRequestDto requestDto
    ) throws PermissionDeniedException {
        Long id = boardService.updateBoard(boardId, requestDto);
        return ResponseModel.success(id);
    }

    @DeleteMapping("/{boardId}")
    @Operation(summary = "[U] 커뮤니티 게시글 삭제", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    public ResponseModel<Long> deleteBoard(
            @PathVariable(name = "boardId") Long boardId
    ) throws PermissionDeniedException {
        Long id = boardService.deleteBoard(boardId);
        return ResponseModel.success(id);
    }

    @PostMapping("/likes/{boardId}")
    @Operation(summary = "[U] 커뮤니티 게시글 좋아요 누르기/취소하기", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    public ResponseModel<Long> toggleLikeStatus(
            @PathVariable(name = "boardId") Long boardId
    ) {
        Long id = boardService.toggleLikeStatus(boardId);
        return ResponseModel.success(id);
    }

}
