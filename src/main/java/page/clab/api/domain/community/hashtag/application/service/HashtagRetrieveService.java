package page.clab.api.domain.community.hashtag.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.domain.community.hashtag.application.dto.mapper.HashtagDtoMapper;
import page.clab.api.domain.community.hashtag.application.dto.response.HashtagResponseDto;
import page.clab.api.domain.community.hashtag.application.port.in.RetrieveHashtagUseCase;
import page.clab.api.domain.community.hashtag.application.port.out.RetrieveHashtagPort;
import page.clab.api.domain.community.hashtag.domain.Hashtag;

@Service
@RequiredArgsConstructor
public class HashtagRetrieveService implements RetrieveHashtagUseCase {

    private final RetrieveHashtagPort retrieveHashtagPort;
    private final HashtagDtoMapper mapper;

    @Override
    public Boolean existsByName(String name) {
        return retrieveHashtagPort.existsByName(name);
    }

    @Override
    public Hashtag getByName(String name) {
        return retrieveHashtagPort.getByName(name);
    }

    @Override
    public List<HashtagResponseDto> retrieveHashtagWithUsedBoardCount() {
        List<Hashtag> hashtags = retrieveHashtagPort.findAllByOrderById();
        return hashtags.stream()
            .map(mapper::toDto)
            .toList();
    }
}
