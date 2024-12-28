package page.clab.api.domain.community.hashtag.application.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import page.clab.api.domain.community.hashtag.domain.HashtagCategory;

@Getter
@Setter
public class HashtagRequestDto {

    private String name;

    @NotNull
    private HashtagCategory hashtagCategory;
}
