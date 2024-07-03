package page.clab.api.domain.accuse.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.accuse.application.port.in.RetrieveAccusationUseCase;
import page.clab.api.domain.accuse.application.port.out.RetrieveAccuseByTargetPort;
import page.clab.api.domain.accuse.application.port.out.RetrieveAccuseTargetsByConditionsPort;
import page.clab.api.domain.accuse.domain.Accuse;
import page.clab.api.domain.accuse.domain.AccuseStatus;
import page.clab.api.domain.accuse.domain.AccuseTarget;
import page.clab.api.domain.accuse.domain.TargetType;
import page.clab.api.domain.accuse.dto.response.AccuseResponseDto;
import page.clab.api.domain.member.application.port.in.RetrieveMemberInfoUseCase;
import page.clab.api.domain.member.dto.shared.MemberBasicInfoDto;
import page.clab.api.global.common.dto.PagedResponseDto;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AccusationRetrievalService implements RetrieveAccusationUseCase {

    private final RetrieveAccuseTargetsByConditionsPort retrieveAccuseTargetsByConditionsPort;
    private final RetrieveAccuseByTargetPort retrieveAccuseByTargetPort;
    private final RetrieveMemberInfoUseCase retrieveMemberInfoUseCase;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<AccuseResponseDto> retrieveAccusations(TargetType type, AccuseStatus status, boolean countOrder, Pageable pageable) {
        Page<AccuseTarget> accuseTargets = retrieveAccuseTargetsByConditionsPort.findByConditions(type, status, countOrder, pageable);
        List<AccuseResponseDto> responseDtos = convertTargetsToResponseDtos(accuseTargets);
        return new PagedResponseDto<>(responseDtos, pageable, responseDtos.size());
    }

    private List<AccuseResponseDto> convertTargetsToResponseDtos(Page<AccuseTarget> accuseTargets) {
        return accuseTargets.stream()
                .map(accuseTarget -> {
                    List<Accuse> accuses = retrieveAccuseByTargetPort.findByTargetOrderByCreatedAtDesc(accuseTarget.getTargetType(), accuseTarget.getTargetReferenceId());
                    if (accuses.isEmpty()) {
                        return null;
                    }
                    List<MemberBasicInfoDto> members = accuses.stream()
                            .map(accuse -> retrieveMemberInfoUseCase.getMemberBasicInfoById(accuse.getMemberId()))
                            .toList();
                    return AccuseResponseDto.toDto(accuses.getFirst(), members);
                })
                .filter(Objects::nonNull)
                .toList();
    }
}
