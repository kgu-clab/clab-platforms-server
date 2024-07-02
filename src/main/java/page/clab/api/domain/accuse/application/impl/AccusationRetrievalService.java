package page.clab.api.domain.accuse.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.accuse.application.AccusationRetrievalUseCase;
import page.clab.api.domain.accuse.dao.AccuseRepository;
import page.clab.api.domain.accuse.dao.AccuseTargetRepository;
import page.clab.api.domain.accuse.domain.Accuse;
import page.clab.api.domain.accuse.domain.AccuseStatus;
import page.clab.api.domain.accuse.domain.AccuseTarget;
import page.clab.api.domain.accuse.domain.TargetType;
import page.clab.api.domain.accuse.dto.response.AccuseResponseDto;
import page.clab.api.domain.member.application.MemberLookupUseCase;
import page.clab.api.domain.member.dto.shared.MemberBasicInfoDto;
import page.clab.api.global.common.dto.PagedResponseDto;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AccusationRetrievalService implements AccusationRetrievalUseCase {

    private final AccuseRepository accuseRepository;
    private final AccuseTargetRepository accuseTargetRepository;
    private final MemberLookupUseCase memberLookupUseCase;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<AccuseResponseDto> retrieve(TargetType type, AccuseStatus status, boolean countOrder, Pageable pageable) {
        Page<AccuseTarget> accuseTargets = accuseTargetRepository.findByConditions(type, status, countOrder, pageable);
        List<AccuseResponseDto> responseDtos = convertTargetsToResponseDtos(accuseTargets);
        return new PagedResponseDto<>(responseDtos, pageable, responseDtos.size());
    }

    private List<AccuseResponseDto> convertTargetsToResponseDtos(Page<AccuseTarget> accuseTargets) {
        return accuseTargets.stream()
                .map(accuseTarget -> {
                    List<Accuse> accuses = accuseRepository.findByTargetOrderByCreatedAtDesc(accuseTarget.getTargetType(), accuseTarget.getTargetReferenceId());
                    if (accuses.isEmpty()) {
                        return null;
                    }
                    List<MemberBasicInfoDto> members = accuses.stream()
                            .map(accuse -> memberLookupUseCase.getMemberBasicInfoById(accuse.getMemberId()))
                            .toList();
                    return AccuseResponseDto.toDto(accuses.getFirst(), members);
                })
                .filter(Objects::nonNull)
                .toList();
    }
}