package page.clab.api.domain.application.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.application.application.port.in.RetrieveDeletedApplicationsUseCase;
import page.clab.api.domain.application.application.port.out.RetrieveApplicationPort;
import page.clab.api.domain.application.domain.Application;
import page.clab.api.domain.application.application.dto.response.ApplicationResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeletedApplicationRetrievalService implements RetrieveDeletedApplicationsUseCase {

    private final RetrieveApplicationPort retrieveApplicationPort;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<ApplicationResponseDto> retrieveDeletedApplications(Pageable pageable) {
        Page<Application> applications = retrieveApplicationPort.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(applications.map(ApplicationResponseDto::toDto));
    }
}
