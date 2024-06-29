package page.clab.api.domain.news.application;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.news.dto.response.NewsDetailsResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface DeletedNewsRetrievalService {
    PagedResponseDto<NewsDetailsResponseDto> retrieve(Pageable pageable);
}