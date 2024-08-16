package page.clab.api.global.common.slack.application;

import jakarta.servlet.http.HttpServletRequest;
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
import page.clab.api.global.config.SlackConfig;

@Service
public class SlackService {

    private final ApplicationEventPublisher eventPublisher;
    private final String coreTeamWebhookUrl;
    private final String executivesWebhookUrl;

    public SlackService(ApplicationEventPublisher eventPublisher, SlackConfig slackConfig) {
        this.eventPublisher = eventPublisher;
        this.coreTeamWebhookUrl = slackConfig.getCoreTeamWebhookUrl();
        this.executivesWebhookUrl = slackConfig.getExecutivesWebhookUrl();
    }

    public void sendServerErrorNotification(HttpServletRequest request, Exception e) {
        eventPublisher.publishEvent(new NotificationEvent(this, coreTeamWebhookUrl, GeneralAlertType.SERVER_ERROR, request, e));
    }

    public void sendSecurityAlertNotification(HttpServletRequest request, SecurityAlertType alertType, String additionalMessage) {
        eventPublisher.publishEvent(new NotificationEvent(this, coreTeamWebhookUrl, alertType, request, additionalMessage));
    }

    public void sendAdminLoginNotification(HttpServletRequest request, MemberLoginInfoDto loginMember) {
        eventPublisher.publishEvent(new NotificationEvent(this, coreTeamWebhookUrl, GeneralAlertType.ADMIN_LOGIN, request, loginMember));
    }

    public void sendNewApplicationNotification(ApplicationRequestDto applicationRequestDto) {
        eventPublisher.publishEvent(new NotificationEvent(this, executivesWebhookUrl, ExecutivesAlertType.NEW_APPLICATION, null, applicationRequestDto));
    }

    public void sendNewBoardNotification(SlackBoardInfo board) {
        eventPublisher.publishEvent(new NotificationEvent(this, executivesWebhookUrl, ExecutivesAlertType.NEW_BOARD, null, board));
    }

    public void sendNewMembershipFeeNotification() {
        eventPublisher.publishEvent(new NotificationEvent(this, executivesWebhookUrl, ExecutivesAlertType.NEW_MEMBERSHIP_FEE, null, null));
    }

    @EventListener(ContextRefreshedEvent.class)
    public void sendServerStartNotification() {
        eventPublisher.publishEvent(new NotificationEvent(this, coreTeamWebhookUrl, GeneralAlertType.SERVER_START, null, null));
    }
}
