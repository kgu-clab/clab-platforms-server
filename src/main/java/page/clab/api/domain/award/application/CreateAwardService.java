package page.clab.api.domain.award.application;

import page.clab.api.domain.award.dto.request.AwardRequestDto;

public interface CreateAwardService {
    Long execute(AwardRequestDto requestDto);
}
