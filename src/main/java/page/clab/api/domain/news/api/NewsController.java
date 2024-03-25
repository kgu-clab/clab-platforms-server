package page.clab.api.domain.news.api;

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
import page.clab.api.domain.news.application.NewsService;
import page.clab.api.domain.news.dto.request.NewsRequestDto;
import page.clab.api.domain.news.dto.request.NewsUpdateRequestDto;
import page.clab.api.domain.news.dto.response.NewsDetailsResponseDto;
import page.clab.api.domain.news.dto.response.NewsResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.dto.ResponseModel;

@RestController
@RequestMapping("/api/v1/news")
@RequiredArgsConstructor
@Tag(name = "News", description = "뉴스")
@Slf4j
public class NewsController {

    private final NewsService newsService;

    @Operation(summary = "뉴스 등록", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping("")
    public ResponseModel createNews(
            @Valid @RequestBody NewsRequestDto requestDto
    ) {
        Long id = newsService.createNews(requestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "[U] 뉴스 목록 조회(제목, 카테고리 기준)", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "2개의 파라미터를 자유롭게 조합하여 필터링 가능<br>" +
            "제목, 카테고리 중 하나라도 입력하지 않으면 전체 조회됨")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("")
    public ResponseModel getNewsByConditions(
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<NewsResponseDto> news = newsService.getNewsByConditions(title, category, pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(news);
        return responseModel;
    }

    @Operation(summary = "[U] 뉴스 상세 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/{newsId}")
    public ResponseModel getNewsDetails(
            @PathVariable(name = "newsId") Long newsId
    ) {
        NewsDetailsResponseDto news = newsService.getNewsDetails(newsId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(news);
        return responseModel;
    }

    @Operation(summary = "[A] 뉴스 수정", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @PatchMapping("/{newsId}")
    public ResponseModel updateNews(
            @PathVariable(name = "newsId") Long newsId,
            @Valid @RequestBody NewsUpdateRequestDto requestDto
    ) {
        Long id = newsService.updateNews(newsId, requestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "[A] 뉴스 삭제", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @DeleteMapping("/{newsId}")
    public ResponseModel deleteNews(
            @PathVariable(name = "newsId") Long newsId
    ) {
        Long id = newsService.deleteNews(newsId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

}
