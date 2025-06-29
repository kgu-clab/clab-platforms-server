package page.clab.api.domain.members.support.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.members.support.application.port.in.RemoveAnswerUseCase;
import page.clab.api.domain.members.support.application.port.out.RegisterAnswerPort;
import page.clab.api.domain.members.support.application.port.out.RegisterSupportPort;
import page.clab.api.domain.members.support.application.port.out.RetrieveAnswerPort;
import page.clab.api.domain.members.support.application.port.out.RetrieveSupportPort;
import page.clab.api.domain.members.support.domain.Answer;
import page.clab.api.domain.members.support.domain.Support;

@Service
@RequiredArgsConstructor
public class AnswerRemoveService implements RemoveAnswerUseCase {

    private final RetrieveAnswerPort retrieveAnswerPort;
    private final RegisterAnswerPort registerAnswerPort;
    private final RetrieveSupportPort retrieveSupportPort;
    private final RegisterSupportPort registerSupportPort;

    @Transactional
    @Override
    public Long removeAnswer(Long supportId) {
        Answer answer = retrieveAnswerPort.findAnswerBySupportId(supportId);
        Support support = retrieveSupportPort.getById(supportId);
        answer.delete();
        support.markAsPending();
        registerSupportPort.save(support);
        return registerAnswerPort.save(answer).getId();
    }
}
