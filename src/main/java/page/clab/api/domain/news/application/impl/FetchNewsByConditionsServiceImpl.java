package page.clab.api.domain.news.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.news.application.FetchNewsByConditionsService;
import page.clab.api.domain.news.dao.NewsRepository;
import page.clab.api.domain.news.domain.News;
import page.clab.api.domain.news.dto.response.NewsResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class FetchNewsByConditionsServiceImpl implements FetchNewsByConditionsService {

    private final NewsRepository newsRepository;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<NewsResponseDto> execute(String title, String category, Pageable pageable) {
        Page<News> newsPage = newsRepository.findByConditions(title, category, pageable);
        return new PagedResponseDto<>(newsPage.map(NewsResponseDto::toDto));
    }
}
