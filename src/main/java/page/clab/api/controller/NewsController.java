package page.clab.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import page.clab.api.type.dto.NewsDto;
import page.clab.api.type.dto.ResponseModel;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
@Tag(name = "News")
@Slf4j
public class NewsController {

    private final NewsService newsService;

    @PostMapping("")
    @Operation(summary = "뉴스 생성", description = "뉴스 생성" +
            "String category;<br>" +
            "String title;<br>" +
            "String subtitle;<br>" +
            "String content;<br>" +
            "String url;<br>" +
            "LocalDateTime createdAt;<br>" +
            "LocalDateTime updateTime;<br>")
    public ResponseModel createNews(@RequestBody NewsDto newsDto) throws PermissionDeniedException {
        newsService.createNews(newsDto);
        return ResponseModel.builder().build();
    }

    @Operation(summary = "뉴스 리스트 조회", description = "뉴스 리스트 조회")
    @GetMapping("")
    public ResponseModel readNewsList() {
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(newsService.readNewsList());
        return responseModel;
    }

    @Operation(summary = "뉴스 상세 조회", description = "뉴스 상세 조회")
    @GetMapping("/{newsId}")
    public ResponseModel readNews(@PathVariable("newsId") Long newsId) {
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(newsService.readNewsById(newsId));
        return responseModel;
    }

    @Operation(summary = "뉴스 검색", description = "뉴스 검색, 제목과 카테고리 기반 검색")
    @GetMapping("/search")
    public ResponseModel searchNews(@RequestParam(required = false) String title,
                                    @RequestParam(required = false) String category) {
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(newsService.searchNewsByTitleAndCategory(title, category));
        return responseModel;
    }

    @PatchMapping("/{newsId}")
    @Operation(summary = "뉴스 수정", description = "뉴스 수정" +
            "String category;<br>" +
            "String title;<br>" +
            "String subtitle;<br>" +
            "String content;<br>" +
            "String url;<br>" +
            "LocalDateTime createdAt;<br>" +
            "LocalDateTime updateTime;<br>")
    public ResponseModel updateNews(@PathVariable("newsId") Long newsId, @RequestBody NewsDto newsDto) throws PermissionDeniedException{
        newsService.updateNews(newsId, newsDto);
        return ResponseModel.builder().build();
    }

    @Operation(summary = "뉴스 삭제", description = "뉴스 삭제")
    @DeleteMapping("/{newsId}")
    public ResponseModel deleteNews(@PathVariable("newsId") Long newsId) throws PermissionDeniedException{
        newsService.deleteNews(newsId);
        return ResponseModel.builder().build();
    }

}
