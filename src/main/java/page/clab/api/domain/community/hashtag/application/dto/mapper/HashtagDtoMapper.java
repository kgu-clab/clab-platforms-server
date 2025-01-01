package page.clab.api.domain.community.hashtag.application.dto.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import page.clab.api.domain.community.hashtag.application.dto.response.HashtagResponseDto;
import page.clab.api.domain.community.hashtag.domain.Hashtag;
import page.clab.api.domain.community.hashtag.domain.HashtagCategory;

@Component
@RequiredArgsConstructor
public class HashtagDtoMapper {

    public Hashtag of(String name, HashtagCategory hashtagCategory) {
        return Hashtag.builder()
            .name(name)
            .hashtagCategory(hashtagCategory)
            .boardUsage(0L)
            .isDeleted(false)
            .build();
    }

    public HashtagResponseDto toDto(Hashtag hashtag) {
        return HashtagResponseDto.builder()
            .id(hashtag.getId())
            .name(hashtag.getName())
            .hashtagCategory(hashtag.getHashtagCategory().getKey())
            .boardUsageCount(hashtag.getBoardUsage())
            .build();
    }
}
