package page.clab.api.domain.community.comment.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.community.comment.application.dto.request.CommentRequestDto;
import page.clab.api.domain.community.comment.application.port.in.RegisterCommentUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
@Tag(name = "Community - Comment", description = "커뮤니티 댓글")
public class CommentRegisterController {

    private final RegisterCommentUseCase registerCommentUseCase;

    @Operation(summary = "[U] 댓글 생성", description = "ROLE_USER 이상의 권한이 필요함")
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{boardId}")
    public ApiResponse<Long> registerComment(
            @RequestParam(name = "parentId", required = false) Long parentId,
            @PathVariable(name = "boardId") Long boardId,
            @Valid @RequestBody CommentRequestDto requestDto
    ) {
        Long id = registerCommentUseCase.registerComment(parentId, boardId, requestDto);
        return ApiResponse.success(id);
    }
}
