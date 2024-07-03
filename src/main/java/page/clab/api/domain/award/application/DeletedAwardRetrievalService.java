package page.clab.api.domain.award.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.award.application.port.in.RetrieveDeletedAwardsUseCase;
import page.clab.api.domain.award.application.port.out.RetrieveDeletedAwardsPort;
import page.clab.api.domain.award.domain.Award;
import page.clab.api.domain.award.dto.response.AwardResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class DeletedAwardRetrievalService implements RetrieveDeletedAwardsUseCase {

    private final RetrieveDeletedAwardsPort retrieveDeletedAwardsPort;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<AwardResponseDto> retrieve(Pageable pageable) {
        Page<Award> awards = retrieveDeletedAwardsPort.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(awards.map(AwardResponseDto::toDto));
    }
}
