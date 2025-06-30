package page.clab.api.domain.members.support.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.domain.members.support.application.dto.mapper.SupportDtoMapper;
import page.clab.api.domain.members.support.application.dto.response.SupportDetailsResponseDto;
import page.clab.api.domain.members.support.application.port.in.RetrieveSupportDetailsUseCase;
import page.clab.api.domain.members.support.application.port.out.RetrieveSupportAnswerPort;
import page.clab.api.domain.members.support.application.port.out.RetrieveSupportPort;
import page.clab.api.domain.members.support.domain.SupportAnswer;
import page.clab.api.domain.members.support.domain.Support;
import page.clab.api.domain.members.support.domain.SupportCategory;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.global.exception.BaseException;
import page.clab.api.global.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class SupportDetailsRetrievalService implements RetrieveSupportDetailsUseCase {

    private final RetrieveSupportPort retrieveSupportPort;
    private final RetrieveSupportAnswerPort retrieveSupportAnswerPort;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;
    private final SupportDtoMapper mapper;

    @Transactional(readOnly = true)
    @Override
    public SupportDetailsResponseDto retrieveSupportDetails(Long supportId) {
        MemberDetailedInfoDto currentMemberInfo = externalRetrieveMemberUseCase.getCurrentMemberDetailedInfo();
        Support support = retrieveSupportPort.getById(supportId);

        validateAccessible(currentMemberInfo,support);

        MemberDetailedInfoDto boardMemberInfo = getMemberDetailedInfoByBoard(support);

        SupportAnswer supportAnswer = retrieveSupportAnswerPort.findAnswerBySupportId(supportId);
        boolean isOwner = support.isOwner(currentMemberInfo.getMemberId());
        return mapper.toDto(support, boardMemberInfo, isOwner, supportAnswer);
    }

    private MemberDetailedInfoDto getMemberDetailedInfoByBoard(Support support) {
        return externalRetrieveMemberUseCase.getMemberDetailedInfoById(support.getMemberId());
    }

    private void validateAccessible(MemberDetailedInfoDto memberInfo, Support support) {
        if(!memberInfo.isAdminRole() && isBugCategory(support)) {
            throw new BaseException(ErrorCode.ACCESS_DENIED);
        }
    }

    private boolean isBugCategory(Support support) {
        return support.getCategory() == SupportCategory.BUG;
    }
}
