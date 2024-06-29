package page.clab.api.domain.news.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.news.application.DeleteNewsService;
import page.clab.api.domain.news.dao.NewsRepository;
import page.clab.api.domain.news.domain.News;
import page.clab.api.global.exception.NotFoundException;

@Service
@RequiredArgsConstructor
public class DeleteNewsServiceImpl implements DeleteNewsService {

    private final NewsRepository newsRepository;

    @Transactional
    @Override
    public Long execute(Long newsId) {
        News news = getNewsByIdOrThrow(newsId);
        news.delete();
        return newsRepository.save(news).getId();
    }

    private News getNewsByIdOrThrow(Long newsId) {
        return newsRepository.findById(newsId)
                .orElseThrow(() -> new NotFoundException("해당 뉴스가 존재하지 않습니다."));
    }
}
