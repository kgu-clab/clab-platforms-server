package page.clab.api.domain.application.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.application.application.port.in.RemoveApplicationUseCase;
import page.clab.api.domain.application.application.port.out.RegisterApplicationPort;
import page.clab.api.domain.application.application.port.out.RetrieveApplicationPort;
import page.clab.api.domain.application.domain.Application;
import page.clab.api.domain.application.domain.ApplicationId;

@Service
@RequiredArgsConstructor
public class ApplicationRemoveService implements RemoveApplicationUseCase {

    private final RetrieveApplicationPort retrieveApplicationPort;
    private final RegisterApplicationPort registerApplicationPort;

    @Transactional
    @Override
    public String remove(Long recruitmentId, String studentId) {
        Application application = retrieveApplicationPort.findByIdOrThrow(ApplicationId.create(studentId, recruitmentId));
        application.delete();
        return registerApplicationPort.save(application).getStudentId();
    }
}
