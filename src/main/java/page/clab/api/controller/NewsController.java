package page.clab.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import page.clab.api.service.NewsService;
import page.clab.api.type.dto.NewsDetailsResponseDto;
import page.clab.api.type.dto.NewsRequestDto;
import page.clab.api.type.dto.NewsResponseDto;
import page.clab.api.type.dto.PagedResponseDto;
import page.clab.api.type.dto.ResponseModel;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
@Tag(name = "News", description = "뉴스 관련 API")
@Slf4j
public class NewsController {

    private final NewsService newsService;

    @Operation(summary = "뉴스 등록", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @PostMapping("")
    public ResponseModel createNews(
            @Valid @RequestBody NewsRequestDto newsRequestDto,
            BindingResult result
    ) throws MethodArgumentNotValidException, PermissionDeniedException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        Long id = newsService.createNews(newsRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "[U] 뉴스 목록 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @GetMapping("")
    public ResponseModel getNews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<NewsResponseDto> news = newsService.getNews(pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(news);
        return responseModel;
    }

    @Operation(summary = "[U] 뉴스 상세 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @GetMapping("/{newsId}")
    public ResponseModel getNewsDetails(
            @PathVariable Long newsId
    ) {
        NewsDetailsResponseDto news = newsService.getNewsDetails(newsId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(news);
        return responseModel;
    }

    @Operation(summary = "[U] 뉴스 검색", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "카테고리, 제목을 기준으로 검색")
    @GetMapping("/search")
    public ResponseModel searchNews(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<NewsResponseDto> news = newsService.searchNews(category, title, pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(news);
        return responseModel;
    }

    @Operation(summary = "[A] 뉴스 수정", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @PatchMapping("/{newsId}")
    public ResponseModel updateNews(
            @PathVariable Long newsId,
            @Valid @RequestBody NewsRequestDto newsRequestDto,
            BindingResult result
    ) throws MethodArgumentNotValidException, PermissionDeniedException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        Long id = newsService.updateNews(newsId, newsRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "[A] 뉴스 삭제", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @DeleteMapping("/{newsId}")
    public ResponseModel deleteNews(
            @PathVariable Long newsId
    ) throws PermissionDeniedException {
        Long id = newsService.deleteNews(newsId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

}
