package page.clab.api.domain.community.hashtag.adapter.out.persistence;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import page.clab.api.domain.community.hashtag.application.port.out.RegisterHashtagPort;
import page.clab.api.domain.community.hashtag.application.port.out.RetrieveHashtagPort;
import page.clab.api.domain.community.hashtag.domain.Hashtag;
import page.clab.api.global.exception.BaseException;
import page.clab.api.global.exception.ErrorCode;

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
    public Hashtag getByName(String name) {
        HashtagJpaEntity entity = hashtagRepository.findByName(name);
        if (entity == null) {
            throw new BaseException(ErrorCode.NOT_FOUND, "[Hashtag] name: " + name + "에 해당하는 해시태그가 존재하지 않습니다.");
        }
        return hashtagMapper.toDomain(entity);
    }

    @Override
    public Hashtag getById(Long id) {
        return hashtagRepository.findById(id)
            .map(hashtagMapper::toDomain)
            .orElseThrow(
                () -> new BaseException(ErrorCode.NOT_FOUND, "[Hashtag] id: " + id + "에 해당하는 해시태그가 존재하지 않습니다."));

    }

    @Override
    public Boolean existsById(Long id) {
        return hashtagRepository.existsById(id);
    }

    @Override
    public List<Hashtag> findAllByOrderById() {
        return hashtagRepository.findAllByOrderById()
            .stream().map(hashtagMapper::toDomain)
            .toList();
    }
}
