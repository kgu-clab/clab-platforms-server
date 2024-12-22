package page.clab.api.domain.community.hashtag.application.port.in;

import java.util.List;
import page.clab.api.domain.community.hashtag.application.dto.request.HashtagRequestDto;
import page.clab.api.domain.community.hashtag.application.dto.response.HashtagResponseDto;

public interface RegisterHashtagUseCase {

    List<HashtagResponseDto> registerHashtag(HashtagRequestDto requestDto);
}
