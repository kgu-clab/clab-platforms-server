package page.clab.api.domain.news.application;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.domain.news.dao.NewsRepository;
import page.clab.api.domain.news.domain.News;
import page.clab.api.domain.news.dto.request.NewsRequestDto;
import page.clab.api.domain.news.dto.request.NewsUpdateRequestDto;
import page.clab.api.domain.news.dto.response.NewsDetailsResponseDto;
import page.clab.api.domain.news.dto.response.NewsResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.file.application.FileService;
import page.clab.api.global.common.file.domain.UploadedFile;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.SearchResultNotExistException;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;

    private final FileService fileService;

    public Long createNews(NewsRequestDto newsRequestDto) {
        News news = News.of(newsRequestDto);
        List<String> fileUrls = newsRequestDto.getFileUrlList();
        if (fileUrls != null) {
            List<UploadedFile> uploadFileList =  fileUrls.stream()
                    .map(fileService::getUploadedFileByUrl)
                    .collect(Collectors.toList());
            news.setUploadedFiles(uploadFileList);
        }
        return newsRepository.save(news).getId();
    }

    public PagedResponseDto<NewsResponseDto> getNews(Pageable pageable) {
        Page<News> news = newsRepository.findAllByOrderByCreatedAtDesc(pageable);
        return new PagedResponseDto<>(news.map(NewsResponseDto::of));
    }

    public NewsDetailsResponseDto getNewsDetails(Long newsId) {
        News news = getNewsByIdOrThrow(newsId);
        return NewsDetailsResponseDto.of(news);
    }

    public PagedResponseDto<NewsResponseDto> searchNews(String category, String title, Pageable pageable) {
        Page<News> news;
        if (category != null) {
            news = getNewsByCategory(category, pageable);
        } else if (title != null) {
            news = getNewsByTitleContaining(title, pageable);
        } else {
            throw new IllegalArgumentException("적어도 category, title 중 하나를 제공해야 합니다.");
        }
        if (news.isEmpty()) {
            throw new SearchResultNotExistException("검색 결과가 존재하지 않습니다.");
        }
        return new PagedResponseDto<>(news.map(NewsResponseDto::of));
    }

    public Long updateNews(Long newsId, NewsUpdateRequestDto newsUpdateRequestDto) {
        News news = getNewsByIdOrThrow(newsId);
        news.update(newsUpdateRequestDto);
        return newsRepository.save(news).getId();
    }

    public Long deleteNews(Long newsId) {
        News news = getNewsByIdOrThrow(newsId);
        newsRepository.delete(news);
        return news.getId();
    }

    public News getNewsByIdOrThrow(Long newsId) {
        return newsRepository.findById(newsId)
                .orElseThrow(() -> new NotFoundException("해당 뉴스가 존재하지 않습니다."));
    }

    public boolean isNewsExist(Long newsId) {
        return newsRepository.existsById(newsId);
    }

    private Page<News> getNewsByCategory(String category, Pageable pageable) {
        return newsRepository.findByCategoryOrderByCreatedAtDesc(category, pageable);
    }

    private Page<News> getNewsByTitleContaining(String title, Pageable pageable) {
        return newsRepository.findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(title, pageable);
    }

}