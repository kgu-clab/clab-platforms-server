package page.clab.api.domain.community.hashtag.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import page.clab.api.domain.community.hashtag.application.port.out.RegisterHashtagPort;
import page.clab.api.domain.community.hashtag.application.port.out.RetrieveHashtagPort;
import page.clab.api.domain.community.hashtag.domain.Hashtag;

@Component
@RequiredArgsConstructor
public class HashtagPersistenceAdapter implements
        RegisterHashtagPort,
        RetrieveHashtagPort {

    private final HashtagRepository hashtagRepository;
    private final HashtagMapper hashtagMapper;

    @Override
    public Hashtag save(Hashtag hashtag) {
        HashtagJpaEntity entity = hashtagMapper.toEntity(hashtag);
        HashtagJpaEntity savedEntity = hashtagRepository.save(entity);
        return hashtagMapper.toDomain(savedEntity);
    }

    @Override
    public Boolean existsByName(String name) {
        return hashtagRepository.existsByName(name);
    }

    @Override
    public Hashtag findByName(String name) {
        HashtagJpaEntity entity = hashtagRepository.findByName(name);
        return hashtagMapper.toDomain(entity);
    }
}
