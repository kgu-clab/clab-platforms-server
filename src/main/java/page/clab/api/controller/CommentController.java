package page.clab.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import page.clab.api.service.CommentService;
import page.clab.api.type.dto.CommentRequestDto;
import page.clab.api.type.dto.CommentResponseDto;
import page.clab.api.type.dto.ResponseModel;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
@Tag(name = "Comment")
@Slf4j
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 생성", description = "댓글 생성<br> +" +
            "String content;<br>" +
            "String writer;<br>" +
            "Long boardId;<br>" )
    @PostMapping("")
    public ResponseModel createComment(
            @RequestParam Long boardId,
            @RequestBody CommentRequestDto commentRequestDto
    ) {
        commentService.createComment(boardId, commentRequestDto);
        return ResponseModel.builder().build();
    }

    @Operation(summary = "댓글 리스트 조회", description = "댓글 리스트 조회")
    @GetMapping("")
    public ResponseModel getComments(
            @RequestParam Long boardId
    ) {
        ResponseModel responseModel = ResponseModel.builder().build();
        List<CommentResponseDto> comments = commentService.getComments(boardId);
        responseModel.addData(comments);
        return responseModel;
    }

    @Operation(summary = "댓글 수정", description = "댓글 수정<br> +" +
            "String content;<br>" +
            "String writer;<br>" +
            "Long boardId;<br>" )
    @PatchMapping("")
    public ResponseModel updateComment(
            @RequestParam Long commentId,
            @RequestBody CommentRequestDto commentRequestDto
    ) throws PermissionDeniedException {
        commentService.updateComment(commentId, commentRequestDto);
        return ResponseModel.builder().build();
    }

    @Operation(summary = "댓글 삭제", description = "댓글 삭제")
    @DeleteMapping("/{commentId}")
    public ResponseModel deleteComment(
            @PathVariable("commentId") Long commentId
    ) throws PermissionDeniedException {
        commentService.deleteComment(commentId);
        return ResponseModel.builder().build();
    }
}
