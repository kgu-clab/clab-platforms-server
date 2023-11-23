package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.exception.NotFoundException;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.exception.SearchResultNotExistException;
import page.clab.api.repository.NewsRepository;
import page.clab.api.type.dto.NewsDetailsResponseDto;
import page.clab.api.type.dto.NewsRequestDto;
import page.clab.api.type.dto.NewsResponseDto;
import page.clab.api.type.dto.PagedResponseDto;
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
            news = getNewsByCategoryContaining(category, pageable);
        } else if (title != null) {
            news = getNewsByTitleContaining(title, pageable);
        } else {
            throw new IllegalArgumentException("적어도 newsId, category, title 중 하나를 제공해야 합니다.");
        }
        if (news.isEmpty()) {
            throw new SearchResultNotExistException("검색 결과가 존재하지 않습니다.");
        }
        return new PagedResponseDto<>(news.map(NewsResponseDto::of));
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

    private Page<News> getNewsByCategoryContaining(String category, Pageable pageable) {
        return newsRepository.findByCategoryContainingOrderByCreatedAtDesc(category, pageable);
    }

    private Page<News> getNewsByTitleContaining(String title, Pageable pageable) {
        return newsRepository.findByTitleContainingOrderByCreatedAtDesc(title, pageable);
    }

}
