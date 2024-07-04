package page.clab.api.domain.news.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.news.application.port.in.RemoveNewsUseCase;
import page.clab.api.domain.news.application.port.out.RegisterNewsPort;
import page.clab.api.domain.news.application.port.out.RetrieveNewsPort;
import page.clab.api.domain.news.domain.News;

@Service
@RequiredArgsConstructor
public class NewsRemoveService implements RemoveNewsUseCase {

    private final RetrieveNewsPort retrieveNewsPort;
    private final RegisterNewsPort registerNewsPort;

    @Transactional
    @Override
    public Long removeNews(Long newsId) {
        News news = retrieveNewsPort.findByIdOrThrow(newsId);
        news.delete();
        return registerNewsPort.save(news).getId();
    }
}
