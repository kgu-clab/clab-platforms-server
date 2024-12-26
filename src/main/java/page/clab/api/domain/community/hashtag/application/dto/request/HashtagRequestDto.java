package page.clab.api.domain.community.hashtag.application.dto.request;

import lombok.Getter;
import lombok.Setter;
import page.clab.api.domain.community.hashtag.domain.HashtagCategory;

@Getter
@Setter
public class HashtagRequestDto {

    private String name;
    private HashtagCategory hashtagCategory;
}
