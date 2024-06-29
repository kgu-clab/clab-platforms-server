package page.clab.api.domain.news.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.news.application.UpdateNewsService;
import page.clab.api.domain.news.dto.request.NewsUpdateRequestDto;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/news")
@RequiredArgsConstructor
@Tag(name = "News", description = "뉴스")
public class UpdateNewsController {

    private final UpdateNewsService updateNewsService;

    @Operation(summary = "[A] 뉴스 수정", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @PatchMapping("/{newsId}")
    public ApiResponse<Long> updateNews(
            @PathVariable(name = "newsId") Long newsId,
            @Valid @RequestBody NewsUpdateRequestDto requestDto
    ) {
        Long id = updateNewsService.execute(newsId, requestDto);
        return ApiResponse.success(id);
    }
}

