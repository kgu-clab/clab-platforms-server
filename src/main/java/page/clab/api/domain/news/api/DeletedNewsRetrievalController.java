package page.clab.api.domain.news.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.news.application.port.in.RetrieveDeletedNewsUseCase;
import page.clab.api.domain.news.dto.response.NewsDetailsResponseDto;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.dto.PagedResponseDto;

@RestController
@RequestMapping("/api/v1/news")
@RequiredArgsConstructor
@Tag(name = "News", description = "뉴스")
public class DeletedNewsRetrievalController {

    private final RetrieveDeletedNewsUseCase retrieveDeletedNewsUseCase;

    @GetMapping("/deleted")
    @Operation(summary = "[S] 삭제된 뉴스 조회하기", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    public ApiResponse<PagedResponseDto<NewsDetailsResponseDto>> retrieveDeletedNews(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<NewsDetailsResponseDto> pagedNews = retrieveDeletedNewsUseCase.retrieve(pageable);
        return ApiResponse.success(pagedNews);
    }
}
