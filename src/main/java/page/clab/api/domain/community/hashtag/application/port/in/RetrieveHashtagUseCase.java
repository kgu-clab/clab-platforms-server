package page.clab.api.domain.community.hashtag.application.port.in;

import java.util.List;
import page.clab.api.domain.community.hashtag.application.dto.response.HashtagResponseDto;
import page.clab.api.domain.community.hashtag.domain.Hashtag;

public interface RetrieveHashtagUseCase {

    Boolean existsByName(String name);

    Hashtag getByName(String name);

    List<HashtagResponseDto> registerHashtagWithUsedBoardCount();
}
