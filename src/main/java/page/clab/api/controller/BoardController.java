package page.clab.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.service.BoardService;
import page.clab.api.type.dto.BoardRequestDto;
import page.clab.api.type.dto.BoardResonseDto;
import page.clab.api.type.dto.PagedResponseDto;
import page.clab.api.type.dto.ResponseModel;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
@Tag(name = "Board", description = "커뮤니티 게시판 관련 API")
@Slf4j
public class BoardController {

    private final BoardService boardService;
    
    @Operation(summary = "[U] 커뮤니티 게시판 생성", description = "ROLE_USER 이상의 권한이 필요함")
    @PostMapping("")
    public ResponseModel createBoard(
            @Valid @RequestBody BoardRequestDto boardRequestDto,
            BindingResult result
    ) throws MethodArgumentNotValidException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        boardService.createBoard(boardRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @GetMapping("")
    @Operation(summary = "[U] 커뮤니티 게시판 목록 조회", description = "ROLE_USER 이상의 권한이 필요함")
    public ResponseModel getBoards(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<BoardResonseDto> boards = boardService.getBoards(pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(boards);
        return responseModel;
    }

    @GetMapping("/my-boards")
    @Operation(summary = "[U] 내가 쓴 커뮤니티 게시글 조회", description = "ROLE_USER 이상의 권한이 필요함")
    public ResponseModel getMyBoards(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<BoardResonseDto> board = boardService.getMyBoards(pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(board);
        return responseModel;
    }

    @GetMapping("/search")
    @Operation(summary = "[U] 커뮤니티 게시판 검색", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "게시판 ID, 카테고리를 기준으로 검색")
    public ResponseModel searchBoards(
            @RequestParam(required = false) Long boardId,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<BoardResonseDto> boards = boardService.searchBoards(boardId, category, pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(boards);
        return responseModel;
    }

    @PatchMapping("/{boardId}")
    @Operation(summary = "[U] 커뮤니티 게시판 수정", description = "ROLE_USER 이상의 권한이 필요함")
    public ResponseModel updateBoard(
            @PathVariable Long boardId,
            @Valid @RequestBody BoardRequestDto boardDto,
            BindingResult result
    ) throws MethodArgumentNotValidException, PermissionDeniedException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        boardService.updateBoard(boardId, boardDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @DeleteMapping("/{boardId}")
    @Operation(summary = "[U] 커뮤니티 게시판 삭제", description = "ROLE_USER 이상의 권한이 필요함")
    public ResponseModel deleteBoard(
            @PathVariable Long boardId
    ) throws PermissionDeniedException {
        boardService.deleteBoard(boardId);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

}
