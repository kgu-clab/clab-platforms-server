package page.clab.api.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.exception.NotFoundException;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.exception.SearchResultNotExistException;
import page.clab.api.repository.NewsRepository;
import page.clab.api.type.dto.NewsRequestDto;
import page.clab.api.type.dto.NewsResponseDto;
import page.clab.api.type.entity.Member;
import page.clab.api.type.entity.News;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final MemberService memberService;

    private final NewsRepository newsRepository;

    public void createNews(NewsRequestDto newsRequestDto) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        if (!memberService.isMemberAdminRole(member)) {
            throw new PermissionDeniedException("관리자만 뉴스를 등록할 수 있습니다.");
        }
        News news = News.of(newsRequestDto);
        newsRepository.save(news);
    }

    public List<NewsResponseDto> getNews() {
        List<News> news = newsRepository.findAll();
        return news.stream()
                .map(NewsResponseDto::of)
                .collect(Collectors.toList());
    }

    public List<NewsResponseDto> searchNews(Long newsId, String category, String title) {
        List<News> news = new ArrayList<>();
        if (newsId != null) {
            news.add(getNewsByIdOrThrow(newsId));
        } else if (category != null) {
            news.addAll(getNewsByCategory(category));
        } else if (title != null) {
            news.addAll(getNewsByTitle(title));
        } else {
            throw new IllegalArgumentException("적어도 newsId, category, title 중 하나를 제공해야 합니다.");
        }
        if (news.isEmpty()) {
            throw new SearchResultNotExistException();
        }
        return news.stream()
                .map(NewsResponseDto::of)
                .collect(Collectors.toList());
    }

    public void updateNews(Long newsId, NewsRequestDto newsRequestDto) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        if (!memberService.isMemberAdminRole(member)) {
            throw new PermissionDeniedException("관리자만 뉴스를 수정할 수 있습니다.");
        }
        News news = getNewsByIdOrThrow(newsId);
        News updatedNews = News.of(newsRequestDto);
        updatedNews.setId(news.getId());
        updatedNews.setCreatedAt(news.getCreatedAt());
        updatedNews.setUpdateTime(LocalDateTime.now());
        newsRepository.save(updatedNews);
    }

    public void deleteNews(Long newsId) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        if (!memberService.isMemberAdminRole(member)) {
            throw new PermissionDeniedException("관리자만 뉴스를 삭제할 수 있습니다.");
        }
        News news = getNewsByIdOrThrow(newsId);
        newsRepository.delete(news);
    }

    public News getNewsByIdOrThrow(Long newsId) {
        return newsRepository.findById(newsId)
                .orElseThrow(() -> new NotFoundException("해당 뉴스가 존재하지 않습니다."));
    }

    private List<News> getNewsByCategory(String category) {
        return newsRepository.findByCategoryContaining(category);
    }

    private List<News> getNewsByTitle(String title) {
        return newsRepository.findByTitleContaining(title);
    }

}
