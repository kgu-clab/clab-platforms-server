package page.clab.api.domain.news.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.news.application.port.in.NewsByConditionsRetrievalUseCase;
import page.clab.api.domain.news.application.port.out.RetrieveNewsByConditionsPort;
import page.clab.api.domain.news.domain.News;
import page.clab.api.domain.news.dto.response.NewsResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class NewsByConditionsRetrievalService implements NewsByConditionsRetrievalUseCase {

    private final RetrieveNewsByConditionsPort retrieveNewsByConditionsPort;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<NewsResponseDto> retrieve(String title, String category, Pageable pageable) {
        Page<News> newsPage = retrieveNewsByConditionsPort.findByConditions(title, category, pageable);
        return new PagedResponseDto<>(newsPage.map(NewsResponseDto::toDto));
    }
}
