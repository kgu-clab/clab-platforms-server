package page.clab.api.domain.community.hashtag.application.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.domain.community.hashtag.application.dto.mapper.HashtagDtoMapper;
import page.clab.api.domain.community.hashtag.application.dto.request.HashtagRequestDto;
import page.clab.api.domain.community.hashtag.application.dto.response.HashtagResponseDto;
import page.clab.api.domain.community.hashtag.application.port.in.RegisterHashtagUseCase;
import page.clab.api.domain.community.hashtag.application.port.in.RetrieveHashtagUseCase;
import page.clab.api.domain.community.hashtag.application.port.out.RegisterHashtagPort;
import page.clab.api.domain.community.hashtag.domain.Hashtag;
import page.clab.api.domain.community.hashtag.domain.HashtagCategory;

@Service
@RequiredArgsConstructor
public class HashtagRegisterService implements RegisterHashtagUseCase {

    private final RegisterHashtagPort registerHashtagPort;
    private final RetrieveHashtagUseCase retrieveHashtagUseCase;
    private final HashtagDtoMapper mapper;

    @Override
    public List<HashtagResponseDto> registerHashtag(List<HashtagRequestDto> requestDtos) {
        List<HashtagResponseDto> savedHashtag = new ArrayList<>();
        for (HashtagRequestDto requestDto : requestDtos) {
            String name = requestDto.getName();
            HashtagCategory hashtagCategory = requestDto.getHashtagCategory();
            if (retrieveHashtagUseCase.existsByName(name)) {
                savedHashtag.add(mapper.toDto(retrieveHashtagUseCase.getByName(name)));
            } else {
                Hashtag hashtag = mapper.of(name, hashtagCategory);
                savedHashtag.add(mapper.toDto(registerHashtagPort.save(hashtag)));
            }
        }
        return savedHashtag;
    }
}
