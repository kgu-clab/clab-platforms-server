package page.clab.api.global.common.slack.application;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import page.clab.api.domain.community.board.domain.SlackBoardInfo;
import page.clab.api.domain.hiring.application.application.dto.request.ApplicationRequestDto;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberLoginInfoDto;
import page.clab.api.global.common.slack.domain.ExecutivesAlertType;
import page.clab.api.global.common.slack.domain.GeneralAlertType;
import page.clab.api.global.common.slack.domain.SecurityAlertType;
import page.clab.api.global.common.slack.event.NotificationEvent;

@Service
@RequiredArgsConstructor
@Slf4j
public class SlackService {

    private final ApplicationEventPublisher eventPublisher;

    public void sendServerErrorNotification(HttpServletRequest request, Exception e) {
        eventPublisher.publishEvent(new NotificationEvent(this, GeneralAlertType.SERVER_ERROR, request, e));
    }

    public void sendSecurityAlertNotification(HttpServletRequest request, SecurityAlertType alertType, String additionalMessage) {
        eventPublisher.publishEvent(new NotificationEvent(this, alertType, request, additionalMessage));
    }

    public void sendAdminLoginNotification(HttpServletRequest request, MemberLoginInfoDto loginMember) {
        eventPublisher.publishEvent(new NotificationEvent(this, GeneralAlertType.ADMIN_LOGIN, request, loginMember));
    }

    public void sendNewApplicationNotification(ApplicationRequestDto applicationRequestDto) {
        eventPublisher.publishEvent(new NotificationEvent(this, ExecutivesAlertType.NEW_APPLICATION, null, applicationRequestDto));
    }

    public void sendNewBoardNotification(SlackBoardInfo board) {
        eventPublisher.publishEvent(new NotificationEvent(this, ExecutivesAlertType.NEW_BOARD, null, board));
    }

    public void sendNewMembershipFeeNotification() {
        eventPublisher.publishEvent(new NotificationEvent(this, ExecutivesAlertType.NEW_MEMBERSHIP_FEE, null, null));
    }

    @EventListener(ContextRefreshedEvent.class)
    public void sendServerStartNotification() {
        eventPublisher.publishEvent(new NotificationEvent(this, GeneralAlertType.SERVER_START, null, null));
    }
}
