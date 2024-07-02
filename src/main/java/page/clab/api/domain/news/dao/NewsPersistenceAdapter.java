package page.clab.api.domain.news.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import page.clab.api.domain.news.application.port.out.LoadNewsPort;
import page.clab.api.domain.news.application.port.out.RegisterNewsPort;
import page.clab.api.domain.news.application.port.out.RemoveNewsPort;
import page.clab.api.domain.news.application.port.out.RetrieveDeletedNewsPort;
import page.clab.api.domain.news.application.port.out.RetrieveNewsByConditionsPort;
import page.clab.api.domain.news.application.port.out.UpdateNewsPort;
import page.clab.api.domain.news.domain.News;
import page.clab.api.global.exception.NotFoundException;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class NewsPersistenceAdapter implements
        RegisterNewsPort,
        LoadNewsPort,
        RemoveNewsPort,
        RetrieveDeletedNewsPort,
        RetrieveNewsByConditionsPort,
        UpdateNewsPort {

    private final NewsRepository repository;

    @Override
    public News save(News news) {
        return repository.save(news);
    }

    @Override
    public void delete(News news) {
        repository.delete(news);
    }

    @Override
    public News update(News news) {
        return repository.save(news);
    }

    @Override
    public Optional<News> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public News findByIdOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("[News] id: " + id + "에 해당하는 뉴스가 존재하지 않습니다."));
    }

    @Override
    public Page<News> findByConditions(String title, String category, Pageable pageable) {
        return repository.findByConditions(title, category, pageable);
    }

    @Override
    public Page<News> findAllByIsDeletedTrue(Pageable pageable) {
        return repository.findAllByIsDeletedTrue(pageable);
    }
}
