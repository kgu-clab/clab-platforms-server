package page.clab.api.domain.application.application.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.application.application.DeletedApplicationRetrievalService;
import page.clab.api.domain.application.dao.ApplicationRepository;
import page.clab.api.domain.application.domain.Application;
import page.clab.api.domain.application.dto.response.ApplicationResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeletedApplicationRetrievalServiceImpl implements DeletedApplicationRetrievalService {

    private final ApplicationRepository applicationRepository;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<ApplicationResponseDto> retrieve(Pageable pageable) {
        Page<Application> applications = applicationRepository.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(applications.map(ApplicationResponseDto::toDto));
    }
}