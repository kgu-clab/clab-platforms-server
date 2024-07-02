package page.clab.api.domain.award.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.award.application.port.in.MyAwardRetrievalUseCase;
import page.clab.api.domain.award.application.port.out.RetrieveMyAwardsPort;
import page.clab.api.domain.award.domain.Award;
import page.clab.api.domain.award.dto.response.AwardResponseDto;
import page.clab.api.domain.member.application.MemberLookupUseCase;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class MyAwardRetrievalService implements MyAwardRetrievalUseCase {

    private final RetrieveMyAwardsPort retrieveMyAwardsPort;
    private final MemberLookupUseCase memberLookupUseCase;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<AwardResponseDto> retrieve(Pageable pageable) {
        String currentMemberId = memberLookupUseCase.getCurrentMemberId();
        Page<Award> awards = retrieveMyAwardsPort.findByMemberId(currentMemberId, pageable);
        return new PagedResponseDto<>(awards.map(AwardResponseDto::toDto));
    }
}
