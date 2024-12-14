package page.clab.api.domain.community.hashtag.application.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.domain.community.hashtag.application.dto.mapper.HashtagDtoMapper;
import page.clab.api.domain.community.hashtag.application.dto.request.HashtagRequestDto;
import page.clab.api.domain.community.hashtag.application.port.in.RegisterHashtagUseCase;
import page.clab.api.domain.community.hashtag.application.port.in.RetrieveHashtagUseCase;
import page.clab.api.domain.community.hashtag.application.port.out.RegisterHashtagPort;
import page.clab.api.domain.community.hashtag.domain.Hashtag;

@Service
@RequiredArgsConstructor
public class HashtagRegisterService implements RegisterHashtagUseCase {

    private final RegisterHashtagPort registerHashtagPort;
    private final RetrieveHashtagUseCase retrieveHashtagUseCase;
    private final HashtagDtoMapper mapper;

    @Override
    public List<Long> registerHashtag(HashtagRequestDto requestDto) {
        List<Long> savedHashtagId = new ArrayList<>();
        for (String name : requestDto.getHashtagNames()) {
            if (retrieveHashtagUseCase.existsByName(name)) {
                savedHashtagId.add(retrieveHashtagUseCase.getByName(name).getId());
            } else {
                Hashtag hashtag = mapper.fromDto(name);
                Long id = registerHashtagPort.save(hashtag).getId();
                savedHashtagId.add(id);
            }
        }
        return savedHashtagId;
    }
}
