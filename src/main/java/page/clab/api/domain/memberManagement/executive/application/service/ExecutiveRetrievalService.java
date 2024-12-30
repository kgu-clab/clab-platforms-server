package page.clab.api.domain.memberManagement.executive.application.service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.executive.application.dto.mapper.ExecutiveDtoMapper;
import page.clab.api.domain.memberManagement.executive.application.dto.response.ExecutiveResponseDto;
import page.clab.api.domain.memberManagement.executive.application.port.in.RetrieveExecutiveUseCase;
import page.clab.api.domain.memberManagement.executive.application.port.out.RetrieveExecutivePort;
import page.clab.api.domain.memberManagement.executive.domain.Executive;
import page.clab.api.external.memberManagement.position.application.port.ExternalRetrievePositionUseCase;

@Service
@RequiredArgsConstructor
public class ExecutiveRetrievalService implements RetrieveExecutiveUseCase {

    private final RetrieveExecutivePort retrieveExecutivePort;
    private final ExternalRetrievePositionUseCase externalRetrievePositionUseCase;
    private final ExecutiveDtoMapper mapper;
    private static final Map<String, Integer> PRIORITY = Map.of(
        "PRESIDENT", 1,
        "VICE_PRESIDENT", 2,
        "OPERATION", 3
    );

    @Transactional(readOnly = true)
    @Override
    public List<ExecutiveResponseDto> retrieveExecutives() {
        List<Executive> executives = retrieveExecutivePort.findAll();
        Map<String, String> positionMap = getPositionMap(executives);
        List<Executive> sortedExecutives = sortExecutives(executives, positionMap);

        return sortedExecutives.stream()
            .map(executive -> mapper.toDto(executive, positionMap.get(executive.getId())))
            .toList();
    }

    private List<Executive> sortExecutives(List<Executive> executives, Map<String, String> positionMap) {
        return executives.stream()
            .sorted(Comparator
                .comparing((Executive executive) ->
                    PRIORITY.getOrDefault(positionMap.get(executive.getId()), Integer.MAX_VALUE))
                .thenComparing(Executive::getId))
            .toList();
    }

    private Map<String, String> getPositionMap(List<Executive> executives) {
        return executives.stream()
            .collect(Collectors.toMap(
                Executive::getId,
                executive -> externalRetrievePositionUseCase
                    .findTopByMemberIdAndYearOrderByCreatedAtDesc(executive.getId(),
                        String.valueOf(LocalDate.now().getYear()))
                    .map(position -> position.getPositionType().getKey())
                    .orElse("")
            ));
    }
}
