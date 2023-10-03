package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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

    public void createNews(NewsDto newsDto){
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

    public NewsDto readNews(Long newsId){
        News news = getNewsById(newsId);
        return NewsDto.of(news);
    }

    public List<NewsDto> searchTitle(String title){
        List<News> newsList = newsRepository.findAllByTitle(title);
        List<NewsDto> newsDtoList = new ArrayList<>();
        for (News news : newsList) {
            NewsDto newsDto = NewsDto.of(news);
            newsDtoList.add(newsDto);
        }
        return newsDtoList;
    }

    public List<NewsDto> searchCategory(String category){
        List<News> newsList = newsRepository.findAllByCategory(category);
        List<NewsDto> newsDtoList = new ArrayList<>();
        for (News news : newsList) {
            NewsDto newsDto = NewsDto.of(news);
            newsDtoList.add(newsDto);
        }
        return newsDtoList;
    }

    public void updateNews(Long newsId, NewsDto newsDto){
        News news = getNewsById(newsId);
        news.setTitle(newsDto.getTitle());
        news.setContent(newsDto.getContent());
        news.setCategory(newsDto.getCategory());
        news.setSubtitle(newsDto.getSubtitle());
        news.setUrl(newsDto.getUrl());
        news.setUpdateTime(LocalDateTime.now());
        newsRepository.save(news);
    }

    public void deleteNews(Long newsId){
        News news = getNewsById(newsId);
        newsRepository.delete(news);
    }

    public News getNewsById(Long newsId){
        return newsRepository.findById(newsId).orElseThrow();
    }

}
