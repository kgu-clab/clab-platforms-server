package page.clab.api.domain.members.support.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.members.support.application.port.in.RemoveSupportAnswerUseCase;
import page.clab.api.domain.members.support.application.port.out.RegisterSupportAnswerPort;
import page.clab.api.domain.members.support.application.port.out.RegisterSupportPort;
import page.clab.api.domain.members.support.application.port.out.RetrieveSupportAnswerPort;
import page.clab.api.domain.members.support.application.port.out.RetrieveSupportPort;
import page.clab.api.domain.members.support.domain.SupportAnswer;
import page.clab.api.domain.members.support.domain.Support;

@Service
@RequiredArgsConstructor
public class AnswerRemoveService implements RemoveSupportAnswerUseCase {

    private final RetrieveSupportAnswerPort retrieveSupportAnswerPort;
    private final RegisterSupportAnswerPort registerSupportAnswerPort;
    private final RetrieveSupportPort retrieveSupportPort;
    private final RegisterSupportPort registerSupportPort;

    @Transactional
    @Override
    public Long removeAnswer(Long supportId) {
        SupportAnswer supportAnswer = retrieveSupportAnswerPort.findAnswerBySupportId(supportId);
        Support support = retrieveSupportPort.getById(supportId);
        supportAnswer.delete();
        support.markAsPending();
        registerSupportPort.save(support);
        return registerSupportAnswerPort.save(supportAnswer).getId();
    }
}
