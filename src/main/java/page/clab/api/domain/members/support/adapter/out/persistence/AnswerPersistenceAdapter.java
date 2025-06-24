package page.clab.api.domain.members.support.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import page.clab.api.domain.members.support.application.port.out.RegisterAnswerPort;
import page.clab.api.domain.members.support.application.port.out.RetrieveAnswerPort;
import page.clab.api.domain.members.support.domain.Answer;

@Component
@RequiredArgsConstructor
public class AnswerPersistenceAdapter implements
        RetrieveAnswerPort, RegisterAnswerPort {

    private final AnswerRepository answerRepository;
    private final AnswerMapper answerMapper;

    @Override
    public Answer findAnswerBySupportId(Long supportId) {
        return answerMapper.toDomain(answerRepository.findBySupportId(supportId));
    }

    @Override
    public Answer save(Answer answer) {
        AnswerJpaEntity entity = answerMapper.toEntity(answer);
        AnswerJpaEntity savedEntity = answerRepository.save(entity);
        return answerMapper.toDomain(savedEntity);
    }
}
