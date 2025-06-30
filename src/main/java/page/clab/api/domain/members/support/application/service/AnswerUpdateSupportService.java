package page.clab.api.domain.members.support.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.members.support.application.dto.request.SupportAnswerUpdateRequestDto;
import page.clab.api.domain.members.support.application.port.in.UpdateSupportAnswerUseCase;
import page.clab.api.domain.members.support.application.port.out.RegisterSupportAnswerPort;
import page.clab.api.domain.members.support.application.port.out.RetrieveSupportAnswerPort;
import page.clab.api.domain.members.support.domain.SupportAnswer;

@Service
@RequiredArgsConstructor
public class AnswerUpdateSupportService implements UpdateSupportAnswerUseCase {

    private final RetrieveSupportAnswerPort retrieveSupportAnswerPort;
    private final RegisterSupportAnswerPort registerSupportAnswerPort;

    @Transactional
    @Override
    public Long updateAnswer(Long supportId, SupportAnswerUpdateRequestDto requestDto) {
        SupportAnswer supportAnswer = retrieveSupportAnswerPort.findAnswerBySupportId(supportId);
        supportAnswer.update(requestDto);

        return registerSupportAnswerPort.save(supportAnswer).getSupportId();
    }
}
