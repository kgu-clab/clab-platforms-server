package page.clab.api.domain.news.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.news.application.port.in.NewsRemoveUseCase;
import page.clab.api.domain.news.application.port.out.LoadNewsPort;
import page.clab.api.domain.news.application.port.out.RegisterNewsPort;
import page.clab.api.domain.news.domain.News;

@Service
@RequiredArgsConstructor
public class NewsRemoveService implements NewsRemoveUseCase {

    private final LoadNewsPort loadNewsPort;
    private final RegisterNewsPort registerNewsPort;

    @Transactional
    @Override
    public Long remove(Long newsId) {
        News news = loadNewsPort.findByIdOrThrow(newsId);
        news.delete();
        return registerNewsPort.save(news).getId();
    }
}
