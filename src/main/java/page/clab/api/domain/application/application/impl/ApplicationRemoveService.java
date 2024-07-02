package page.clab.api.domain.application.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.application.application.ApplicationRemoveUseCase;
import page.clab.api.domain.application.dao.ApplicationRepository;
import page.clab.api.domain.application.domain.Application;
import page.clab.api.domain.application.domain.ApplicationId;
import page.clab.api.global.exception.NotFoundException;

@Service
@RequiredArgsConstructor
public class ApplicationRemoveService implements ApplicationRemoveUseCase {

    private final ApplicationRepository applicationRepository;

    @Transactional
    @Override
    public String remove(Long recruitmentId, String studentId) {
        Application application = getApplicationByIdOrThrow(studentId, recruitmentId);
        application.delete();
        applicationRepository.save(application);
        return application.getStudentId();
    }

    private Application getApplicationByIdOrThrow(String studentId, Long recruitmentId) {
        return applicationRepository.findById(ApplicationId.create(studentId, recruitmentId))
                .orElseThrow(() -> new NotFoundException("해당 지원자가 없습니다."));
    }
}