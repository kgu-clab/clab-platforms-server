package page.clab.api.domain.members.support.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.members.support.application.dto.request.AnswerUpdateRequestDto;
import page.clab.api.domain.members.support.application.port.in.UpdateAnswerUseCase;
import page.clab.api.domain.members.support.application.port.out.RegisterAnswerPort;
import page.clab.api.domain.members.support.application.port.out.RetrieveAnswerPort;
import page.clab.api.domain.members.support.domain.Answer;

@Service
@RequiredArgsConstructor
public class AnswerUpdateService implements UpdateAnswerUseCase {

    private final RetrieveAnswerPort retrieveAnswerPort;
    private final RegisterAnswerPort registerAnswerPort;

    @Transactional
    @Override
    public Long updateAnswer(Long supportId, AnswerUpdateRequestDto requestDto) {
        Answer answer = retrieveAnswerPort.findAnswerBySupportId(supportId);
        answer.update(requestDto);

        return registerAnswerPort.save(answer).getSupportId();
    }
}
