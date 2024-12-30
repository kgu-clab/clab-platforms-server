package page.clab.api.domain.memberManagement.executive.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.executive.application.port.in.RemoveExecutiveUseCase;
import page.clab.api.domain.memberManagement.executive.application.port.out.RegisterExecutivePort;
import page.clab.api.domain.memberManagement.executive.application.port.out.RetrieveExecutivePort;
import page.clab.api.domain.memberManagement.executive.domain.Executive;

@Service
@RequiredArgsConstructor
public class ExecutiveRemoveService implements RemoveExecutiveUseCase {

    private final RetrieveExecutivePort retrieveExecutivePort;
    private final RegisterExecutivePort registerExecutivePort;

    @Transactional
    @Override
    public String removeExecutive(String executiveId) {
        Executive executive = retrieveExecutivePort.getById(executiveId);
        executive.delete();
        registerExecutivePort.save(executive);
        return executive.getId();
    }
}
