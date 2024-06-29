package page.clab.api.domain.award.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.award.dao.AwardRepository;
import page.clab.api.domain.award.domain.Award;
import page.clab.api.domain.award.dto.response.AwardResponseDto;
import page.clab.api.domain.member.application.MemberLookupService;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class FetchMyAwardsServiceImpl implements FetchMyAwardsService {

    private final AwardRepository awardRepository;
    private final MemberLookupService memberLookupService;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<AwardResponseDto> execute(Pageable pageable) {
        String currentMemberId = memberLookupService.getCurrentMemberId();
        Page<Award> awards = awardRepository.findByMemberId(currentMemberId, pageable);
        return new PagedResponseDto<>(awards.map(AwardResponseDto::toDto));
    }
}
