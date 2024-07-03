package page.clab.api.domain.news.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.news.application.port.in.UpdateNewsUseCase;
import page.clab.api.domain.news.application.port.out.LoadNewsPort;
import page.clab.api.domain.news.application.port.out.UpdateNewsPort;
import page.clab.api.domain.news.domain.News;
import page.clab.api.domain.news.dto.request.NewsUpdateRequestDto;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class NewsUpdateService implements UpdateNewsUseCase {

    private final ValidationService validationService;
    private final LoadNewsPort loadNewsPort;
    private final UpdateNewsPort updateNewsPort;

    @Transactional
    @Override
    public Long update(Long newsId, NewsUpdateRequestDto requestDto) {
        News news = loadNewsPort.findByIdOrThrow(newsId);
        news.update(requestDto);
        validationService.checkValid(news);
        return updateNewsPort.update(news).getId();
    }
}
