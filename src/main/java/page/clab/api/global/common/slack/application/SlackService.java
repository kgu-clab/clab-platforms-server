package page.clab.api.global.common.slack.application;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import page.clab.api.domain.hiring.application.application.dto.request.ApplicationRequestDto;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberLoginInfoDto;
import page.clab.api.global.common.slack.domain.ExecutivesAlertType;
import page.clab.api.global.common.slack.domain.GeneralAlertType;
import page.clab.api.global.common.slack.domain.SecurityAlertType;
import page.clab.api.global.common.slack.domain.SlackBoardInfo;
import page.clab.api.global.common.slack.domain.SlackBookLoanRecordInfo;
import page.clab.api.global.common.slack.domain.SlackMembershipFeeInfo;
import page.clab.api.global.common.slack.event.NotificationEvent;
import page.clab.api.global.config.SlackConfig;

/**
 * SlackService는 다양한 알림 유형에 따라 Slack 알림을 전송하는 서비스입니다.
 *
 * <p>이 서비스는 `NotificationEvent`를 통해 Slack 알림을 발송하며,
 * 서버 시작, 서버 오류, 보안 경고, 관리자 로그인 등의 알림 유형을 제공합니다.</p>
 *
 * 주요 기능:
 * <ul>
 *     <li>{@link #sendServerErrorNotification(HttpServletRequest, Exception)} - 서버 오류 발생 시 Slack으로 알림을 전송합니다.</li>
 *     <li>{@link #sendSecurityAlertNotification(HttpServletRequest, SecurityAlertType, String)} - 보안 경고 발생 시 알림을 전송합니다.</li>
 *     <li>{@link #sendAdminLoginNotification(HttpServletRequest, MemberLoginInfoDto)} - 관리자 로그인 시 Slack으로 알림을 전송합니다.</li>
 *     <li>{@link #sendNewApplicationNotification(ApplicationRequestDto)} - 신규 지원 정보가 있을 때 Slack으로 알림을 전송합니다.</li>
 *     <li>{@link #sendNewBoardNotification(SlackBoardInfo)} - 새 게시글이 등록되었을 때 알림을 전송합니다.</li>
 *     <li>{@link #sendNewMembershipFeeNotification(SlackMembershipFeeInfo)} - 신규 회비 신청 시 알림을 전송합니다.</li>
 *     <li>{@link #sendNewBookLoanRequestNotification(SlackBookLoanRecordInfo)} - 도서 대여 신청이 있을 때 알림을 전송합니다.</li>
 *     <li>{@link #sendServerStartNotification()} - 서버 시작 시 알림을 전송합니다.</li>
 * </ul>
 */
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

    public void sendNewMembershipFeeNotification(SlackMembershipFeeInfo membershipFee) {
        eventPublisher.publishEvent(new NotificationEvent(this, executivesWebhookUrl, ExecutivesAlertType.NEW_MEMBERSHIP_FEE, null, membershipFee));
    }

    public void sendNewBookLoanRequestNotification(SlackBookLoanRecordInfo bookLoanRecord) {
        eventPublisher.publishEvent(new NotificationEvent(this, executivesWebhookUrl, ExecutivesAlertType.NEW_BOOK_LOAN_REQUEST, null, bookLoanRecord));
    }

    @EventListener(ContextRefreshedEvent.class)
    public void sendServerStartNotification() {
        eventPublisher.publishEvent(new NotificationEvent(this, coreTeamWebhookUrl, GeneralAlertType.SERVER_START, null, null));
    }
}
