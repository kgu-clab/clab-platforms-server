package page.clab.api.domain.application.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.application.application.port.in.DeletedApplicationRetrievalUseCase;
import page.clab.api.domain.application.application.port.out.RetrieveDeletedApplicationsPort;
import page.clab.api.domain.application.domain.Application;
import page.clab.api.domain.application.dto.response.ApplicationResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeletedApplicationRetrievalService implements DeletedApplicationRetrievalUseCase {

    private final RetrieveDeletedApplicationsPort retrieveDeletedApplicationsPort;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<ApplicationResponseDto> retrieve(Pageable pageable) {
        Page<Application> applications = retrieveDeletedApplicationsPort.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(applications.map(ApplicationResponseDto::toDto));
    }
}
