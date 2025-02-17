package page.clab.api.domain.community.board.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.community.board.application.dto.request.BoardRequestDto;
import page.clab.api.domain.community.board.application.port.in.RegisterBoardUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/boards")
@RequiredArgsConstructor
@Tag(name = "Community - Board", description = "커뮤니티 게시판")
public class BoardRegisterController {

    private final RegisterBoardUseCase registerBoardUseCase;

    @Operation(summary = "[U] 커뮤니티 게시글 생성", description = "ROLE_USER 이상의 권한이 필요함")
    @PreAuthorize("hasRole('USER')")
    @PostMapping("")
    public ApiResponse<String> registerBoard(
        @Valid @RequestBody BoardRequestDto requestDto
    ) {
        String id = registerBoardUseCase.registerBoard(requestDto);
        return ApiResponse.success(id);
    }
}
