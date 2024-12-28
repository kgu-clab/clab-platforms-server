package page.clab.api.domain.community.hashtag.application.dto.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import page.clab.api.domain.community.hashtag.application.dto.response.HashtagResponseDto;
import page.clab.api.domain.community.hashtag.domain.Hashtag;

@Component
@RequiredArgsConstructor
public class HashtagDtoMapper {

    public Hashtag of(String name) {
        return Hashtag.builder()
            .name(name)
            .boardUsage(0L)
            .isDeleted(false)
            .build();
    }

    public HashtagResponseDto toDto(Hashtag hashtag) {
        return HashtagResponseDto.builder()
            .id(hashtag.getId())
            .name(hashtag.getName())
            .boardUsageCount(hashtag.getBoardUsage())
            .build();
    }
}
