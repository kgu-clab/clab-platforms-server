package page.clab.api.domain.comment.api;

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
import page.clab.api.domain.board.dto.response.BoardListResponseDto;
import page.clab.api.domain.comment.application.CommentService;
import page.clab.api.domain.comment.dto.request.CommentRequestDto;
import page.clab.api.domain.comment.dto.request.CommentUpdateRequestDto;
import page.clab.api.domain.comment.dto.response.CommentMyResponseDto;
import page.clab.api.domain.comment.dto.response.CommentResponseDto;
import page.clab.api.domain.comment.dto.response.DeletedCommentResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.exception.PermissionDeniedException;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
@Tag(name = "Comment", description = "댓글")
@Slf4j
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "[U] 댓글 생성", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping("/{boardId}")
    public ApiResponse<Long> createComment(
            @RequestParam(name = "parentId", required = false) Long parentId,
            @PathVariable(name = "boardId") Long boardId,
            @Valid @RequestBody CommentRequestDto requestDto
    ) {
        Long id = commentService.createComment(parentId, boardId, requestDto);
        return ApiResponse.success(id);
    }

    @Operation(summary = "[U] 댓글 목록 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/{boardId}")
    public ApiResponse<PagedResponseDto<CommentResponseDto>> getComments(
            @PathVariable(name = "boardId") Long boardId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<CommentResponseDto> comments = commentService.getAllComments(boardId, pageable);
        return ApiResponse.success(comments);
    }

    @Operation(summary = "[U] 나의 댓글 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/my-comments")
    public ApiResponse<PagedResponseDto<CommentMyResponseDto>> getMyComments(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<CommentMyResponseDto> comments = commentService.getMyComments(pageable);
        return ApiResponse.success(comments);
    }

    @Operation(summary = "[U] 댓글 수정", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PatchMapping("/{commentId}")
    public ApiResponse<Long> updateComment(
            @PathVariable(name = "commentId") Long commentId,
            @Valid @RequestBody CommentUpdateRequestDto requestDto
    ) throws PermissionDeniedException {
        Long id = commentService.updateComment(commentId, requestDto);
        return ApiResponse.success(id);
    }

    @Operation(summary = "[U] 댓글 삭제", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @DeleteMapping("/{commentId}")
    public ApiResponse<Long> deleteComment(
            @PathVariable(name = "commentId") Long commentId
    ) throws PermissionDeniedException {
        Long id = commentService.deleteComment(commentId);
        return ApiResponse.success(id);
    }

    @PostMapping("/likes/{commentId}")
    @Operation(summary = "[U] 댓글 좋아요 누르기/취소하기", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    public ApiResponse<Long> toggleLikeStatus(
            @PathVariable(name = "commentId") Long commentId
    ) {
        Long id = commentService.toggleLikeStatus(commentId);
        return ApiResponse.success(id);
    }

    @GetMapping("/deleted/{boardId}")
    @Operation(summary = "[S] 게시글의 삭제된 댓글 조회하기", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    public ApiResponse<PagedResponseDto<DeletedCommentResponseDto>> getDeletedComments(
            @PathVariable(name = "boardId") Long boardId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<DeletedCommentResponseDto> comments = commentService.getDeletedComments(boardId, pageable);
        return ApiResponse.success(comments);
    }

}