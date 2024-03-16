package page.clab.api.domain.news.application;

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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;

    private final FileService fileService;

    public Long createNews(NewsRequestDto newsRequestDto) {
        News news = News.of(newsRequestDto);
        attachUploadedFiles(newsRequestDto, news);
        return newsRepository.save(news).getId();
    }

    public PagedResponseDto<NewsResponseDto> getNewsByConditions(String category, String title, Pageable pageable) {
        Page<News> newsPage = newsRepository.findByConditions(title, category, pageable);
        List<NewsResponseDto> newsResponseDtos = newsPage.getContent().stream()
                .map(NewsResponseDto::of)
                .collect(Collectors.toList());
        return new PagedResponseDto<>(newsResponseDtos, pageable, newsResponseDtos.size());
    }

    public NewsDetailsResponseDto getNewsDetails(Long newsId) {
        News news = getNewsByIdOrThrow(newsId);
        return NewsDetailsResponseDto.of(news);
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

    private void attachUploadedFiles(NewsRequestDto newsRequestDto, News news) {
        List<String> fileUrls = newsRequestDto.getFileUrlList();
        if (fileUrls != null) {
            List<UploadedFile> uploadFileList = fileUrls.stream()
                    .map(fileService::getUploadedFileByUrl)
                    .collect(Collectors.toList());
            news.setUploadedFiles(uploadFileList);
        }
    }

    public News getNewsByIdOrThrow(Long newsId) {
        return newsRepository.findById(newsId)
                .orElseThrow(() -> new NotFoundException("해당 뉴스가 존재하지 않습니다."));
    }

}