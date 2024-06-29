package page.clab.api.domain.comment.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.comment.application.CreateCommentService;
import page.clab.api.domain.comment.dto.request.CommentRequestDto;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
@Tag(name = "Comment", description = "댓글")
public class CreateCommentController {

    private final CreateCommentService createCommentService;

    @Operation(summary = "[U] 댓글 생성", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping("/{boardId}")
    public ApiResponse<Long> createComment(
            @RequestParam(name = "parentId", required = false) Long parentId,
            @PathVariable(name = "boardId") Long boardId,
            @Valid @RequestBody CommentRequestDto requestDto
    ) {
        Long id = createCommentService.execute(parentId, boardId, requestDto);
        return ApiResponse.success(id);
    }
}
