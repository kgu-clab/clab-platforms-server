package page.clab.api.domain.members.support.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.member.domain.Member;
import page.clab.api.domain.members.support.application.port.in.RemoveAnswerUseCase;
import page.clab.api.domain.members.support.application.port.out.RegisterAnswerPort;
import page.clab.api.domain.members.support.application.port.out.RegisterSupportPort;
import page.clab.api.domain.members.support.application.port.out.RetrieveAnswerPort;
import page.clab.api.domain.members.support.application.port.out.RetrieveSupportPort;
import page.clab.api.domain.members.support.domain.Answer;
import page.clab.api.domain.members.support.domain.Support;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;

@Service
@RequiredArgsConstructor
public class AnswerRemoveService implements RemoveAnswerUseCase {

    private final RetrieveAnswerPort retrieveAnswerPort;
    private final RegisterAnswerPort registerAnswerPort;
    private final RetrieveSupportPort retrieveSupportPort;
    private final RegisterSupportPort registerSupportPort;

    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;

    @Transactional
    @Override
    public Long removeAnswer(Long supportId) {
        Member currentMember = externalRetrieveMemberUseCase.getCurrentMember();
        Answer answer = retrieveAnswerPort.findAnswerBySupportId(supportId);
        Support support = retrieveSupportPort.getById(supportId);
        answer.validateAccessPermission(currentMember);
        answer.delete();
        support.removeAnswer();
        registerSupportPort.save(support);
        return registerAnswerPort.save(answer).getId();
    }
}
