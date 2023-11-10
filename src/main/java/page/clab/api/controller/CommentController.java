package page.clab.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.service.CommentService;
import page.clab.api.type.dto.CommentRequestDto;
import page.clab.api.type.dto.CommentResponseDto;
import page.clab.api.type.dto.ResponseModel;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
@Tag(name = "Comment")
@Slf4j
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 생성", description = "댓글 생성")
    @PostMapping("/{boardId}")
    public ResponseModel createComment(
            @PathVariable Long boardId,
            @Valid @RequestBody CommentRequestDto commentRequestDto,
            BindingResult result
    ) throws MethodArgumentNotValidException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        commentService.createComment(boardId, commentRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "댓글 리스트 조회", description = "댓글 리스트 조회")
    @GetMapping("/{boardId}")
    public ResponseModel getComments(
            @PathVariable Long boardId
    ) {
        List<CommentResponseDto> comments = commentService.getComments(boardId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(comments);
        return responseModel;
    }

    @Operation(summary = "나의 댓글 조회", description = "나의 댓글 조회")
    @GetMapping("/my-comments")
    public ResponseModel getMyComments() {
        List<CommentResponseDto> comments = commentService.getMyComments();
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(comments);
        return responseModel;
    }

    @Operation(summary = "댓글 수정", description = "댓글 수정")
    @PatchMapping("/{commentId}")
    public ResponseModel updateComment(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentRequestDto commentRequestDto,
            BindingResult result
    ) throws MethodArgumentNotValidException, PermissionDeniedException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        commentService.updateComment(commentId, commentRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "댓글 삭제", description = "댓글 삭제")
    @DeleteMapping("/{commentId}")
    public ResponseModel deleteComment(
            @PathVariable Long commentId
    ) throws PermissionDeniedException {
        commentService.deleteComment(commentId);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

}
