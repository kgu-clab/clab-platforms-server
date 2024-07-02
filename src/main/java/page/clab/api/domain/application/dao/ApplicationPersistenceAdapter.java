package page.clab.api.domain.application.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import page.clab.api.domain.application.application.port.out.LoadApplicationPort;
import page.clab.api.domain.application.application.port.out.RegisterApplicationPort;
import page.clab.api.domain.application.application.port.out.RemoveApplicationPort;
import page.clab.api.domain.application.application.port.out.RetrieveApplicationsByConditionsPort;
import page.clab.api.domain.application.application.port.out.RetrieveDeletedApplicationsPort;
import page.clab.api.domain.application.domain.Application;
import page.clab.api.domain.application.domain.ApplicationId;
import page.clab.api.global.exception.NotFoundException;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ApplicationPersistenceAdapter implements
        RegisterApplicationPort,
        LoadApplicationPort,
        RetrieveApplicationsByConditionsPort,
        RetrieveDeletedApplicationsPort,
        RemoveApplicationPort {

    private final ApplicationRepository applicationRepository;

    @Override
    public Application save(Application application) {
        return applicationRepository.save(application);
    }

    @Override
    public Optional<Application> findById(ApplicationId applicationId) {
        return applicationRepository.findById(applicationId);
    }

    @Override
    public Application findByIdOrThrow(ApplicationId applicationId) {
        return applicationRepository.findById(applicationId)
                .orElseThrow(() -> new NotFoundException("[Application] id: " + applicationId + "에 해당하는 지원서가 존재하지 않습니다."));
    }

    @Override
    public Page<Application> findByConditions(Long recruitmentId, String studentId, Boolean isPass, Pageable pageable) {
        return applicationRepository.findByConditions(recruitmentId, studentId, isPass, pageable);
    }

    @Override
    public Page<Application> findAllByIsDeletedTrue(Pageable pageable) {
        return applicationRepository.findAllByIsDeletedTrue(pageable);
    }

    @Override
    public void delete(Application application) {
        applicationRepository.delete(application);
    }
}
