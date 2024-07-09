package page.clab.api.domain.award.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.award.application.port.in.RetrieveDeletedAwardsUseCase;
import page.clab.api.domain.award.application.port.out.RetrieveAwardPort;
import page.clab.api.domain.award.domain.Award;
import page.clab.api.domain.award.application.dto.response.AwardResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class DeletedAwardRetrievalService implements RetrieveDeletedAwardsUseCase {

    private final RetrieveAwardPort retrieveAwardPort;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<AwardResponseDto> retrieveDeletedAwards(Pageable pageable) {
        Page<Award> awards = retrieveAwardPort.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(awards.map(AwardResponseDto::toDto));
    }
}
