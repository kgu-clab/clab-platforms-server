package page.clab.api.domain.community.news.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.community.news.application.dto.response.NewsDetailsResponseDto;
import page.clab.api.domain.community.news.application.port.in.RetrieveDeletedNewsUseCase;
import page.clab.api.domain.community.news.application.port.out.RetrieveNewsPort;
import page.clab.api.domain.community.news.domain.News;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class DeletedNewsRetrievalService implements RetrieveDeletedNewsUseCase {

    private final RetrieveNewsPort retrieveNewsPort;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<NewsDetailsResponseDto> retrieveDeletedNews(Pageable pageable) {
        Page<News> newsPage = retrieveNewsPort.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(newsPage.map(NewsDetailsResponseDto::toDto));
    }
}