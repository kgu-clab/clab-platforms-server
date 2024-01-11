package page.clab.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
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
import page.clab.api.service.CommentService;
import page.clab.api.type.dto.CommentGetAllResponseDto;
import page.clab.api.type.dto.CommentGetMyResponseDto;
import page.clab.api.type.dto.CommentRequestDto;
import page.clab.api.type.dto.PagedResponseDto;
import page.clab.api.type.dto.ResponseModel;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
@Tag(name = "Comment", description = "댓글 관련 API")
@Slf4j
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "[U] 댓글 생성", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping("/{boardId}")
    public ResponseModel createComment(
            @RequestParam(required = false) Long parentId,
            @PathVariable Long boardId,
            @Valid @RequestBody CommentRequestDto commentRequestDto,
            BindingResult result
    ) throws MethodArgumentNotValidException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        Long id = commentService.createComment(parentId, boardId, commentRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "[U] 댓글 리스트 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/{boardId}")
    public ResponseModel getComments(
            @PathVariable Long boardId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<CommentGetAllResponseDto> comments = commentService.getComments(boardId, pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(comments);
        return responseModel;
    }

    @Operation(summary = "[U] 나의 댓글 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/my-comments")
    public ResponseModel getMyComments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<CommentGetMyResponseDto> comments = commentService.getMyComments(pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(comments);
        return responseModel;
    }

    @Operation(summary = "[U] 댓글 수정", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PatchMapping("/{commentId}")
    public ResponseModel updateComment(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentRequestDto commentRequestDto,
            BindingResult result
    ) throws MethodArgumentNotValidException, PermissionDeniedException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        Long id = commentService.updateComment(commentId, commentRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "[U] 댓글 삭제", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @DeleteMapping("/{commentId}")
    public ResponseModel deleteComment(
            @PathVariable Long commentId
    ) throws PermissionDeniedException {
        Long id = commentService.deleteComment(commentId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

}