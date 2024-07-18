package page.clab.api.domain.community.news.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import page.clab.api.domain.community.news.application.port.out.RegisterNewsPort;
import page.clab.api.domain.community.news.application.port.out.RetrieveNewsPort;
import page.clab.api.domain.community.news.application.port.out.UpdateNewsPort;
import page.clab.api.domain.community.news.domain.News;
import page.clab.api.global.exception.NotFoundException;

@Component
@RequiredArgsConstructor
public class NewsPersistenceAdapter implements
        RegisterNewsPort,
        UpdateNewsPort,
        RetrieveNewsPort {

    private final NewsRepository repository;
    private final NewsMapper mapper;

    @Override
    public News save(News news) {
        NewsJpaEntity entity = mapper.toJpaEntity(news);
        NewsJpaEntity savedEntity = repository.save(entity);
        return mapper.toDomainEntity(savedEntity);
    }

    @Override
    public News update(News news) {
        NewsJpaEntity entity = mapper.toJpaEntity(news);
        NewsJpaEntity updatedEntity = repository.save(entity);
        return mapper.toDomainEntity(updatedEntity);
    }

    @Override
    public News findByIdOrThrow(Long id) {
        return repository.findById(id)
                .map(mapper::toDomainEntity)
                .orElseThrow(() -> new NotFoundException("[News] id: " + id + "에 해당하는 뉴스가 존재하지 않습니다."));
    }

    @Override
    public Page<News> findByConditions(String title, String category, Pageable pageable) {
        return repository.findByConditions(title, category, pageable)
                .map(mapper::toDomainEntity);
    }

    @Override
    public Page<News> findAllByIsDeletedTrue(Pageable pageable) {
        return repository.findAllByIsDeletedTrue(pageable)
                .map(mapper::toDomainEntity);
    }
}
