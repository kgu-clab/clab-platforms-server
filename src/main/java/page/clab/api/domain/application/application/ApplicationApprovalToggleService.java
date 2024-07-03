package page.clab.api.domain.application.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.application.application.port.in.ToggleApplicationApprovalUseCase;
import page.clab.api.domain.application.application.port.out.LoadApplicationPort;
import page.clab.api.domain.application.application.port.out.RegisterApplicationPort;
import page.clab.api.domain.application.domain.Application;
import page.clab.api.domain.application.domain.ApplicationId;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationApprovalToggleService implements ToggleApplicationApprovalUseCase {

    private final LoadApplicationPort loadApplicationPort;
    private final RegisterApplicationPort registerApplicationPort;
    private final ValidationService validationService;

    @Transactional
    @Override
    public String toggleStatus(Long recruitmentId, String studentId) {
        Application application = loadApplicationPort.findByIdOrThrow(ApplicationId.create(studentId, recruitmentId));
        application.toggleApprovalStatus();
        validationService.checkValid(application);
        return registerApplicationPort.save(application).getStudentId();
    }
}
