package page.clab.api.domain.memberManagement.executive.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.executive.application.dto.mapper.ExecutiveDtoMapper;
import page.clab.api.domain.memberManagement.executive.application.dto.request.ExecutiveRequestDto;
import page.clab.api.domain.memberManagement.executive.application.exception.ExecutiveRegistrationException;
import page.clab.api.domain.memberManagement.executive.application.port.in.RegisterExecutiveUseCase;
import page.clab.api.domain.memberManagement.executive.application.port.out.RegisterExecutivePort;
import page.clab.api.domain.memberManagement.executive.application.port.out.RetrieveExecutivePort;
import page.clab.api.domain.memberManagement.executive.domain.Executive;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;

@Service
@RequiredArgsConstructor
public class ExecutiveRegisterService implements RegisterExecutiveUseCase {

    private final RegisterExecutivePort registerExecutivePort;
    private final RetrieveExecutivePort retrieveExecutivePort;
    private final ExecutiveDtoMapper mapper;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;

    @Transactional
    @Override
    public String registerExecutive(ExecutiveRequestDto requestDto) {
        checkAlreadyExecutive(requestDto);
        checkIsMember(requestDto);
        Executive executive = mapper.fromDto(requestDto);
        Executive savedExecutive = registerExecutivePort.save(executive);
        return savedExecutive.getId();
    }

    private void checkAlreadyExecutive(ExecutiveRequestDto requestDto) {
        if (retrieveExecutivePort.existsById(requestDto.getExecutiveId())) {
            throw new ExecutiveRegistrationException("[Executive] id: " + requestDto.getExecutiveId() + "에 해당하는 운영진이 "
                + "이미 존재합니다.");
        }
    }

    private void checkIsMember(ExecutiveRequestDto requestDto) {
        if (!externalRetrieveMemberUseCase.existsById(requestDto.getExecutiveId())) {
            throw new ExecutiveRegistrationException("[Executive] id: " + requestDto.getExecutiveId() + "에 해당하는 운영진이 "
                + "멤버가 아닙니다.");
        }
    }
}
