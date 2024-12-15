package page.clab.api.domain.community.hashtag.application.port.in;

import java.util.List;
import page.clab.api.domain.community.hashtag.application.dto.request.HashtagRequestDto;

public interface RegisterHashtagUseCase {
    List<Long> registerHashtag(HashtagRequestDto requestDto);
}
