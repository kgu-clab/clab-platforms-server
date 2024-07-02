package page.clab.api.domain.accuse.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.accuse.application.port.in.MyAccusationsUseCase;
import page.clab.api.domain.accuse.application.port.out.RetrieveAccuseByMemberIdPort;
import page.clab.api.domain.accuse.domain.Accuse;
import page.clab.api.domain.accuse.dto.response.AccuseMyResponseDto;
import page.clab.api.domain.member.application.MemberLookupUseCase;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class MyAccusationsService implements MyAccusationsUseCase {

    private final RetrieveAccuseByMemberIdPort retrieveAccuseByMemberIdPort;
    private final MemberLookupUseCase memberLookupUseCase;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<AccuseMyResponseDto> retrieve(Pageable pageable) {
        String currentMemberId = memberLookupUseCase.getCurrentMemberId();
        Page<Accuse> accuses = retrieveAccuseByMemberIdPort.findByMemberId(currentMemberId, pageable);
        return new PagedResponseDto<>(accuses.map(AccuseMyResponseDto::toDto));
    }
}
