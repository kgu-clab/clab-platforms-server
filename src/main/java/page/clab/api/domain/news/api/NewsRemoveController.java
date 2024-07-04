package page.clab.api.domain.news.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.news.application.port.in.RemoveNewsUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/news")
@RequiredArgsConstructor
@Tag(name = "News", description = "뉴스")
public class NewsRemoveController {

    private final RemoveNewsUseCase removeNewsUseCase;

    @Operation(summary = "[A] 뉴스 삭제", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({ "ROLE_ADMIN", "ROLE_SUPER" })
    @DeleteMapping("/{newsId}")
    public ApiResponse<Long> removeNews(
            @PathVariable(name = "newsId") Long newsId
    ) {
        Long id = removeNewsUseCase.removeNews(newsId);
        return ApiResponse.success(id);
    }
}

