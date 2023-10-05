package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.repository.NewsRepository;
import page.clab.api.type.dto.NewsDto;
import page.clab.api.type.entity.News;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;

    private final MemberService memberService;

    public void createNews(NewsDto newsDto) throws PermissionDeniedException {
        memberService.checkMemberAdminRole();
        News news = News.of(newsDto);
        newsRepository.save(news);
    }

    public List<NewsDto> readNewsList(){
        List<News> newsList = newsRepository.findAll();
        List<NewsDto> newsDtoList = new ArrayList<>();
        for (News news : newsList) {
            NewsDto newsDto = NewsDto.of(news);
            newsDtoList.add(newsDto);
        }
        return newsDtoList;
    }

    public NewsDto readNewsById(Long newsId){
        News news = getNewsById(newsId);
        return NewsDto.of(news);
    }

    public List<NewsDto> searchNewsByTitleAndCategory(String title, String category){
        List<News> newsList = newsRepository.findAllByTitleContains(title);
        newsList.addAll(newsRepository.findAllByCategory(category));
        List<NewsDto> newsDtoList = new ArrayList<>();
        for (News news : newsList) {
            NewsDto newsDto = NewsDto.of(news);
            newsDtoList.add(newsDto);
        }
        return newsDtoList;
    }

    public void updateNews(Long newsId, NewsDto newsDto) throws PermissionDeniedException{
        memberService.checkMemberAdminRole();
        News news = getNewsById(newsId);
        News updateNews = News.of(newsDto);
        updateNews.setId(news.getId());
        updateNews.setTitle(newsDto.getTitle());
        updateNews.setContent(newsDto.getContent());
        updateNews.setCategory(newsDto.getCategory());
        updateNews.setSubtitle(newsDto.getSubtitle());
        updateNews.setUrl(newsDto.getUrl());
        updateNews.setUpdateTime(LocalDateTime.now());
        newsRepository.save(updateNews);
    }

    public void deleteNews(Long newsId) throws PermissionDeniedException{
        memberService.checkMemberAdminRole();
        News news = getNewsById(newsId);
        newsRepository.delete(news);
    }

    public News getNewsById(Long newsId){
        return newsRepository.findById(newsId).orElseThrow();
    }

}
