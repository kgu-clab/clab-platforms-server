package page.clab.api.domain.hiring.application.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.hiring.application.adapter.out.persistence.ApplicationId;
import page.clab.api.domain.hiring.application.application.port.in.ToggleApplicationApprovalUseCase;
import page.clab.api.domain.hiring.application.application.port.out.RegisterApplicationPort;
import page.clab.api.domain.hiring.application.application.port.out.RetrieveApplicationPort;
import page.clab.api.domain.hiring.application.domain.Application;

@Service
@RequiredArgsConstructor
public class ApplicationApprovalToggleService implements ToggleApplicationApprovalUseCase {

    private final RetrieveApplicationPort retrieveApplicationPort;
    private final RegisterApplicationPort registerApplicationPort;

    @Transactional
    @Override
    public String toggleApprovalStatus(Long recruitmentId, String studentId) {
        Application application = retrieveApplicationPort.findByIdOrThrow(ApplicationId.create(studentId, recruitmentId));
        application.toggleApprovalStatus();
        return registerApplicationPort.save(application).getStudentId();
    }
}
