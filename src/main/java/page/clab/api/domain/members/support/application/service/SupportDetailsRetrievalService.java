package page.clab.api.domain.members.support.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberBasicInfoDto;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.domain.members.support.application.dto.mapper.SupportDtoMapper;
import page.clab.api.domain.members.support.application.dto.response.SupportDetailsResponseDto;
import page.clab.api.domain.members.support.application.port.in.RetrieveSupportDetailsUseCase;
import page.clab.api.domain.members.support.application.port.out.RetrieveAnswerPort;
import page.clab.api.domain.members.support.application.port.out.RetrieveSupportPort;
import page.clab.api.domain.members.support.domain.Answer;
import page.clab.api.domain.members.support.domain.Support;
import page.clab.api.domain.members.support.domain.SupportCategory;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.global.exception.BaseException;
import page.clab.api.global.exception.ErrorCode;

import java.nio.file.AccessDeniedException;

@Service
@RequiredArgsConstructor
public class SupportDetailsRetrievalService implements RetrieveSupportDetailsUseCase {

    private final RetrieveSupportPort retrieveSupportPort;
    private final RetrieveAnswerPort retrieveAnswerPort;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;
    private final SupportDtoMapper mapper;

    @Transactional(readOnly = true)
    @Override
    public SupportDetailsResponseDto retrieveSupportDetails(Long supportId) {
        MemberDetailedInfoDto currentMemberInfo = externalRetrieveMemberUseCase.getCurrentMemberDetailedInfo();
        Support support = retrieveSupportPort.getById(supportId);

        if(!isAdminRole(currentMemberInfo) && isBugCategory(support)) {
            throw new BaseException(ErrorCode.ACCESS_DENIED);
        }

        Answer answer = retrieveAnswerPort.findAnswerBySupportId(supportId);
        boolean isOwner = support.isOwner(currentMemberInfo.getMemberId());
        return mapper.toDto(support, currentMemberInfo, isOwner, answer);
    }

    private boolean isAdminRole(MemberDetailedInfoDto memberInfo) {
        return memberInfo.isAdminRole();
    }

    private boolean isBugCategory(Support support) {
        return support.getCategory() == SupportCategory.BUG;
    }
}
