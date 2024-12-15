package page.clab.api.domain.community.hashtag.application.dto.request;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HashtagRequestDto {

    List<String> hashtagNames = new ArrayList<>();
}
