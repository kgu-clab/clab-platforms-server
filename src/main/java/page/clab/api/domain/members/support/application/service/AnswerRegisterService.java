package page.clab.api.domain.members.support.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberBasicInfoDto;
import page.clab.api.domain.members.support.application.dto.mapper.AnswerDtoMapper;
import page.clab.api.domain.members.support.application.dto.mapper.SupportDtoMapper;
import page.clab.api.domain.members.support.application.dto.request.AnswerRequestDto;
import page.clab.api.domain.members.support.application.dto.shared.SupportAnswerInfoDto;
import page.clab.api.domain.members.support.application.port.in.RegisterAnswerUseCase;
import page.clab.api.domain.members.support.application.port.out.RegisterAnswerPort;
import page.clab.api.domain.members.support.application.port.out.RegisterSupportPort;
import page.clab.api.domain.members.support.application.port.out.RetrieveSupportPort;
import page.clab.api.domain.members.support.domain.Answer;
import page.clab.api.domain.members.support.domain.Support;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.external.memberManagement.notification.application.port.ExternalSendNotificationUseCase;
import page.clab.api.global.exception.BaseException;
import page.clab.api.global.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class AnswerRegisterService implements RegisterAnswerUseCase {

    private final RegisterAnswerPort registerAnswerPort;
    private final RegisterSupportPort registerSupportPort;
    private final RetrieveSupportPort retrieveSupportPort;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;
    private final ExternalSendNotificationUseCase externalSendNotificationUseCase;
    private final AnswerDtoMapper answerMapper;
    private final SupportDtoMapper supportMapper;

    @Override
    @Transactional
    public Long registerAnswer(Long supportId, AnswerRequestDto requestDto) {
        MemberBasicInfoDto membeInfo = externalRetrieveMemberUseCase.getCurrentMemberBasicInfo();
        Support support = retrieveSupportPort.getById(supportId);

        if(support.hasAnswer()) {
            throw new BaseException(ErrorCode.DUPLICATE_SUPPORT_ANSWER);
        }

        Answer answer = answerMapper.fromDto(membeInfo, supportId, requestDto);
        support.markAsCompleted();
        registerAnswerPort.save(answer);
        registerSupportPort.save(support);
        sendNotificationForNewComment(answer);

        return supportId;
    }

    private void sendNotificationForNewComment(Answer answer) {
        Support support = retrieveSupportPort.getById(answer.getSupportId());
        SupportAnswerInfoDto supportInfo = supportMapper.toDto(support);
        String notificationMessage = String.format("[%s] 문의하신 내용에 답변이 등록되었습니다.", supportInfo.getTitle());
        externalSendNotificationUseCase.sendNotificationToMember(supportInfo.getMemberId(), notificationMessage);
    }
}
