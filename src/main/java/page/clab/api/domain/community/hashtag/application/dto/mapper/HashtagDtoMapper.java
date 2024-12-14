package page.clab.api.domain.community.hashtag.application.dto.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import page.clab.api.domain.community.hashtag.domain.Hashtag;

@Component
@RequiredArgsConstructor
public class HashtagDtoMapper {

    public Hashtag fromDto(String name) {
        return Hashtag.builder()
                .name(name)
                .isDeleted(false)
                .build();
    }
}
