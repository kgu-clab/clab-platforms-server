package page.clab.api.domain.memberManagement.executive.application.service;

import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.executive.application.dto.mapper.ExecutiveDtoMapper;
import page.clab.api.domain.memberManagement.executive.application.dto.response.ExecutiveResponseDto;
import page.clab.api.domain.memberManagement.executive.application.port.in.RetrieveExecutiveUseCase;
import page.clab.api.domain.memberManagement.executive.application.port.out.RetrieveExecutivePort;
import page.clab.api.domain.memberManagement.executive.domain.Executive;

@Service
@RequiredArgsConstructor
public class ExecutiveRetrievalService implements RetrieveExecutiveUseCase {

    private final RetrieveExecutivePort retrieveExecutivePort;
    private final ExecutiveDtoMapper mapper;

    @Transactional(readOnly = true)
    @Override
    public List<ExecutiveResponseDto> retrieveExecutives() {
        List<Executive> executives = retrieveExecutivePort.findAll();
        sortExecutives(executives);
        return executives.stream()
            .map(mapper::toDto)
            .toList();
    }

    private void sortExecutives(List<Executive> executives) {
        executives.sort(Comparator
            .comparing((Executive executive) -> executive.getPosition().ordinal())
            .thenComparing(Executive::getId));
    }
}
