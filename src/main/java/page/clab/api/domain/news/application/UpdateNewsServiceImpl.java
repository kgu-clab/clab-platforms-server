package page.clab.api.domain.news.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.news.dao.NewsRepository;
import page.clab.api.domain.news.domain.News;
import page.clab.api.domain.news.dto.request.NewsUpdateRequestDto;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class UpdateNewsServiceImpl implements UpdateNewsService {

    private final ValidationService validationService;
    private final NewsRepository newsRepository;

    @Transactional
    @Override
    public Long execute(Long newsId, NewsUpdateRequestDto requestDto) {
        News news = getNewsByIdOrThrow(newsId);
        news.update(requestDto);
        validationService.checkValid(news);
        return newsRepository.save(news).getId();
    }

    private News getNewsByIdOrThrow(Long newsId) {
        return newsRepository.findById(newsId)
                .orElseThrow(() -> new NotFoundException("해당 뉴스가 존재하지 않습니다."));
    }
}

