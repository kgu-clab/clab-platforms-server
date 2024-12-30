package page.clab.api.domain.memberManagement.executive.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.executive.application.dto.request.ExecutiveUpdateRequestDto;
import page.clab.api.domain.memberManagement.executive.application.port.in.UpdateExecutiveUseCase;
import page.clab.api.domain.memberManagement.executive.application.port.out.RetrieveExecutivePort;
import page.clab.api.domain.memberManagement.executive.application.port.out.UpdateExecutivePort;
import page.clab.api.domain.memberManagement.executive.domain.Executive;

@Service
@RequiredArgsConstructor
public class ExecutiveUpdateService implements UpdateExecutiveUseCase {

    private final UpdateExecutivePort updateExecutivePort;
    private final RetrieveExecutivePort retrieveExecutivePort;

    @Transactional
    @Override
    public String updateExecutive(String executiveId, ExecutiveUpdateRequestDto requestDto) {
        Executive executive = retrieveExecutivePort.getById(executiveId);
        executive.update(requestDto);
        Executive updatedExecutive = updateExecutivePort.update(executive);
        return updatedExecutive.getId();
    }
}
