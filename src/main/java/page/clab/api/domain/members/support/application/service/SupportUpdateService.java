package page.clab.api.domain.members.support.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.member.domain.Member;
import page.clab.api.domain.members.support.application.dto.request.SupportUpdateRequestDTO;
import page.clab.api.domain.members.support.application.port.in.UpdateSupportUseCase;
import page.clab.api.domain.members.support.application.port.out.RegisterSupportPort;
import page.clab.api.domain.members.support.application.port.out.RetrieveSupportPort;
import page.clab.api.domain.members.support.domain.Support;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;

@Service
@RequiredArgsConstructor
public class SupportUpdateService implements UpdateSupportUseCase {

    private final RetrieveSupportPort retrieveSupportPort;
    private final RegisterSupportPort registerSupportPort;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;

    @Transactional
    @Override
    public Long updateSupport(Long supportId, SupportUpdateRequestDTO requestDto) {
        Member currentMember = externalRetrieveMemberUseCase.getCurrentMember();
        Support support = retrieveSupportPort.getById(supportId);
        support.validateAccessPermission(currentMember);
        support.update(requestDto);
        return registerSupportPort.save(support).getId();
    }
}
