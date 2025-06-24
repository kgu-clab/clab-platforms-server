package page.clab.api.domain.members.support.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberBasicInfoDto;
import page.clab.api.domain.members.support.application.dto.mapper.SupportDtoMapper;
import page.clab.api.domain.members.support.application.dto.response.SupportMyResponseDto;
import page.clab.api.domain.members.support.application.port.in.RetrieveMySupportUseCase;
import page.clab.api.domain.members.support.application.port.out.RetrieveSupportPort;
import page.clab.api.domain.members.support.domain.Support;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class MySupportRetrievalService implements RetrieveMySupportUseCase {

    private final RetrieveSupportPort retrieveSupportPort;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;
    private final SupportDtoMapper mapper;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<SupportMyResponseDto> retrieveMySupports(Pageable pageable) {
        MemberBasicInfoDto currentMemberInfo = externalRetrieveMemberUseCase.getCurrentMemberBasicInfo();
        Page<Support> supports = retrieveSupportPort.findAllByWriterId(currentMemberInfo.getMemberId(), pageable);
        return new PagedResponseDto<>(supports.map(support -> mapper.toDto(support, currentMemberInfo)));
    }
}
