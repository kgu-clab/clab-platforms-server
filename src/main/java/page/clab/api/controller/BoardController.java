package page.clab.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.service.BoardService;
import page.clab.api.service.MemberService;
import page.clab.api.type.dto.BoardDto;
import page.clab.api.type.dto.ResponseModel;
import page.clab.api.type.entity.Member;


@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
@Tag(name = "Board")
@Slf4j
public class BoardController {

    private final BoardService boardService;

    private final MemberService memberService;


    @Operation(summary = "커뮤니티 게시판 생성", description = "커뮤니티 게시판 생성<br>" +
            "String category; <br>" +
            "String title; <br>" +
            "String content; <br>" +
            "LocalDateTime updateTime; <br>" +
            "LocalDateTime createdAt; <br>" +
            "String writer; <br>")
    @PostMapping("")
    public ResponseModel createBoard(@RequestBody BoardDto boardDto) {
        Member member = memberService.getCurrentMember();
        boardService.createBoard(boardDto, member);
        return ResponseModel.builder().build();
    }

    @GetMapping("/{boardId}/detail")
    @Operation(summary = "커뮤니티 게시판 조회", description = "커뮤니티 게시판 조회")
    public ResponseModel getBoards(@PathVariable("boardId") Long boardId) {
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(boardService.getBoards(boardId));
        return responseModel;
    }

    @GetMapping("/list")
    @Operation(summary = "커뮤니티 게시판 리스팅", description = "커뮤니티 게시판 리스팅")
    public ResponseModel getBoardList() {
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(boardService.getBoardList());
        return responseModel;
    }

    @GetMapping("/my")
    @Operation(summary = "내가 쓴 커뮤니티 게시글 조회", description = "내가 쓴 커뮤니티 게시글 조회")
    public ResponseModel getMyBoardList() {
        Member member = memberService.getCurrentMember();
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(boardService.getMyBoardList(member));
        return responseModel;
    }

    @GetMapping("/{category}/list")
    @Operation(summary = "카테고리 별로 게시글 조회", description = "카테고리 별로 게시글 조회")
    public ResponseModel getBoardListByCategory(@PathVariable("category") String category) {
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(boardService.getBoardListByCategory(category));
        return responseModel;
    }

    @PutMapping("{boardId}/update")
    @Operation(summary = "커뮤니티 게시판 수정", description = "커뮤니티 게시판 수정<br>" +
            "String category; <br>" +
            "String title; <br>" +
            "String content; <br>" +
            "LocalDateTime updateTime; <br>" +
            "LocalDateTime createdAt; <br>" +
            "String writer; <br>")
    public ResponseModel updateBoard(@PathVariable("boardId") Long boardId, @RequestBody BoardDto boardDto) {
        Member member = memberService.getCurrentMember();
        boardService.updateBoard(member, boardId, boardDto);
        return ResponseModel.builder().build();
    }

    @DeleteMapping("{boardId}/delete")
    @Operation(summary = "커뮤니티 게시판 삭제", description = "커뮤니티 게시판 삭제")
    public ResponseModel deleteBoard(@PathVariable("boardId") Long boardId) {
        Member member = memberService.getCurrentMember();
        boardService.deleteBoard(member, boardId);
        return ResponseModel.builder().build();
    }


}
