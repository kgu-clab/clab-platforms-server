package page.clab.api.domain.application.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.application.application.port.in.RetrieveApplicationsUseCase;
import page.clab.api.domain.application.application.port.out.RetrieveApplicationPort;
import page.clab.api.domain.application.domain.Application;
import page.clab.api.domain.application.dto.response.ApplicationResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class ApplicationRetrievalService implements RetrieveApplicationsUseCase {

    private final RetrieveApplicationPort retrieveApplicationPort;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<ApplicationResponseDto> retrieveApplications(Long recruitmentId, String studentId, Boolean isPass, Pageable pageable) {
        Page<Application> applications = retrieveApplicationPort.findByConditions(recruitmentId, studentId, isPass, pageable);
        return new PagedResponseDto<>(applications.map(ApplicationResponseDto::toDto));
    }
}
