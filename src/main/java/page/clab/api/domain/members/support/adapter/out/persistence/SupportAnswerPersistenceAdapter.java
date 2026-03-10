package page.clab.api.domain.members.support.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import page.clab.api.domain.members.support.application.port.out.RegisterSupportAnswerPort;
import page.clab.api.domain.members.support.application.port.out.RetrieveSupportAnswerPort;
import page.clab.api.domain.members.support.domain.SupportAnswer;

@Component
@RequiredArgsConstructor
public class SupportAnswerPersistenceAdapter implements
    RetrieveSupportAnswerPort, RegisterSupportAnswerPort {

    private final SupportAnswerRepository supportAnswerRepository;
    private final SupportAnswerMapper supportAnswerMapper;

    @Override
    public SupportAnswer findAnswerBySupportId(Long supportId) {
        return supportAnswerMapper.toDomain(supportAnswerRepository.findBySupportId(supportId));
    }

    @Override
    public SupportAnswer save(SupportAnswer supportAnswer) {
        SupportAnswerJpaEntity entity = supportAnswerMapper.toEntity(supportAnswer);
        SupportAnswerJpaEntity savedEntity = supportAnswerRepository.save(entity);
        return supportAnswerMapper.toDomain(savedEntity);
    }
}
