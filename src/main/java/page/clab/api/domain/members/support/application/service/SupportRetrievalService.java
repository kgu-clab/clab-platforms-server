package page.clab.api.domain.members.support.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberBasicInfoDto;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.domain.members.support.application.dto.mapper.SupportDtoMapper;
import page.clab.api.domain.members.support.application.dto.response.SupportListResponseDto;
import page.clab.api.domain.members.support.application.port.in.RetrieveSupportUseCase;
import page.clab.api.domain.members.support.application.port.out.RetrieveSupportPort;
import page.clab.api.domain.members.support.domain.Support;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class SupportRetrievalService implements RetrieveSupportUseCase {

    private final RetrieveSupportPort retrieveSupportPort;
    private final SupportDtoMapper mapper;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<SupportListResponseDto> retrieveSupports(Pageable pageable) {
        Page<Support> supports;

        if (isCurrentMemberAdmin()) {
            supports = retrieveSupportPort.findAll(pageable);
        } else {
            MemberBasicInfoDto memberInfo = externalRetrieveMemberUseCase.getCurrentMemberBasicInfo();
            supports = retrieveSupportPort.findAllAccessible(memberInfo.getMemberId(), pageable);
        }

        return new PagedResponseDto<>(supports.map(support ->
                mapper.toListDto(support,getMemberDetailedInfoBySupport(support))));
    }

    @Transactional(readOnly = true)
    public Support getById(Long supportId) {
        return retrieveSupportPort.getById(supportId);
    }

    private MemberDetailedInfoDto getMemberDetailedInfoBySupport(Support support) {
        return externalRetrieveMemberUseCase.getMemberDetailedInfoById(support.getMemberId());
    }

    private boolean isCurrentMemberAdmin() {
        MemberDetailedInfoDto memberInfo = externalRetrieveMemberUseCase.getCurrentMemberDetailedInfo();
        return memberInfo.isAdminRole();
    }
}
