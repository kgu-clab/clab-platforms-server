package page.clab.api.domain.community.accuse.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.community.accuse.application.dto.response.AccuseMyResponseDto;
import page.clab.api.domain.community.accuse.application.port.in.RetrieveMyAccusationsUseCase;
import page.clab.api.domain.community.accuse.application.port.out.RetrieveAccusePort;
import page.clab.api.domain.community.accuse.domain.Accuse;
import page.clab.api.domain.member.application.port.in.RetrieveMemberUseCase;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class MyAccusationsService implements RetrieveMyAccusationsUseCase {

    private final RetrieveAccusePort retrieveAccusePort;
    private final RetrieveMemberUseCase retrieveMemberUseCase;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<AccuseMyResponseDto> retrieveMyAccusations(Pageable pageable) {
        String currentMemberId = retrieveMemberUseCase.getCurrentMemberId();
        Page<Accuse> accuses = retrieveAccusePort.findByMemberId(currentMemberId, pageable);
        return new PagedResponseDto<>(accuses.map(AccuseMyResponseDto::toDto));
    }
}
