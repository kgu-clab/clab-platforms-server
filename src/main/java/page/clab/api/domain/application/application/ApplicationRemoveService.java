package page.clab.api.domain.application.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.application.application.port.in.ApplicationRemoveUseCase;
import page.clab.api.domain.application.application.port.out.LoadApplicationPort;
import page.clab.api.domain.application.application.port.out.RegisterApplicationPort;
import page.clab.api.domain.application.domain.Application;
import page.clab.api.domain.application.domain.ApplicationId;

@Service
@RequiredArgsConstructor
public class ApplicationRemoveService implements ApplicationRemoveUseCase {

    private final LoadApplicationPort loadApplicationPort;
    private final RegisterApplicationPort registerApplicationPort;

    @Transactional
    @Override
    public String remove(Long recruitmentId, String studentId) {
        Application application = loadApplicationPort.findByIdOrThrow(ApplicationId.create(studentId, recruitmentId));
        application.delete();
        return registerApplicationPort.save(application).getStudentId();
    }
}
