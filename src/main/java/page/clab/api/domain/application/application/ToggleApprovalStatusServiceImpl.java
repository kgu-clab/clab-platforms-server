package page.clab.api.domain.application.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.application.dao.ApplicationRepository;
import page.clab.api.domain.application.domain.Application;
import page.clab.api.domain.application.domain.ApplicationId;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
@Slf4j
public class ToggleApprovalStatusServiceImpl implements ToggleApprovalStatusService {

    private final ApplicationRepository applicationRepository;
    private final ValidationService validationService;

    @Transactional
    @Override
    public String execute(Long recruitmentId, String studentId) {
        Application application = getApplicationByIdOrThrow(studentId, recruitmentId);
        application.toggleApprovalStatus();
        validationService.checkValid(application);
        return applicationRepository.save(application).getStudentId();
    }

    private Application getApplicationByIdOrThrow(String studentId, Long recruitmentId) {
        return applicationRepository.findById(ApplicationId.create(studentId, recruitmentId))
                .orElseThrow(() -> new NotFoundException("해당 지원자가 없습니다."));
    }
}