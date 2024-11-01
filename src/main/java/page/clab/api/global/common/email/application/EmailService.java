package page.clab.api.global.common.email.application;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import page.clab.api.domain.memberManagement.member.domain.Member;
import page.clab.api.global.common.email.domain.EmailTemplateType;
import page.clab.api.global.common.email.dto.mapper.EmailDtoMapper;
import page.clab.api.global.common.email.dto.request.EmailDto;
import page.clab.api.global.common.email.exception.MessageSendingFailedException;
import page.clab.api.global.config.EmailTemplateProperties;

import java.util.List;

/**
 * EmailService는 사용자의 계정 생성, 비밀번호 재설정, 새 비밀번호 전송을 포함한 이메일 전송을 관리하는 서비스입니다.
 *
 * <p>이 클래스는 템플릿을 기반으로 이메일 콘텐츠를 생성하고, 비동기적으로 이메일을 전송하는 {@link EmailAsyncService}를 활용하여
 * 이메일을 전송합니다. 각 이메일 유형에 대해 적절한 템플릿을 적용하여 이메일의 제목과 내용을 동적으로 설정합니다.</p>
 *
 * 주요 기능:
 * <ul>
 *     <li>{@link #sendAccountCreationEmail(Member, String)} - 계정 생성 시 계정 정보가 포함된 이메일을 전송합니다.</li>
 *     <li>{@link #sendPasswordResetCodeEmail(Member, String)} - 비밀번호 재설정 코드가 포함된 이메일을 전송합니다.</li>
 *     <li>{@link #sendNewPasswordEmail(Member, String)} - 새 비밀번호가 포함된 이메일을 전송합니다.</li>
 *     <li>{@link #generateEmailContent(EmailDto, String)} - 템플릿을 사용하여 이메일 콘텐츠를 생성합니다.</li>
 * </ul>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final EmailAsyncService emailAsyncService;
    private final EmailTemplateProperties emailTemplateProperties;
    private final SpringTemplateEngine springTemplateEngine;
    private final EmailDtoMapper mapper;

    /**
     * 계정 생성 안내 이메일을 전송합니다.
     *
     * @param member 생성된 계정의 멤버 정보
     * @param password 생성된 비밀번호
     */
    public void sendAccountCreationEmail(Member member, String password) {
        EmailTemplateProperties.Template template = emailTemplateProperties.getTemplate(EmailTemplateType.ACCOUNT_CREATION);

        String subject = template.getSubject();
        String content = template.getContent()
                .replace("{{id}}", member.getId())
                .replace("{{password}}", password);

        EmailDto emailDto = mapper.of(
                List.of(member.getEmail()),
                subject,
                content,
                EmailTemplateType.ACCOUNT_CREATION
        );

        sendEmail(emailDto, member, " 계정 발급 안내 메일 전송에 실패했습니다.");
    }

    /**
     * 비밀번호 재설정 코드가 포함된 이메일을 전송합니다.
     *
     * @param member 비밀번호 재설정을 요청한 멤버 정보
     * @param code 비밀번호 재설정 인증 코드
     */
    public void sendPasswordResetCodeEmail(Member member, String code) {
        EmailTemplateProperties.Template template = emailTemplateProperties.getTemplate(EmailTemplateType.PASSWORD_RESET_CODE);

        String subject = template.getSubject();
        String content = template.getContent()
                .replace("{{code}}", code);

        EmailDto emailDto = mapper.of(
                List.of(member.getEmail()),
                subject,
                content,
                EmailTemplateType.PASSWORD_RESET_CODE
        );

        sendEmail(emailDto, member, " 비밀번호 재발급 인증 메일 전송에 실패했습니다.");
    }

    /**
     * 새 비밀번호가 포함된 이메일을 전송합니다.
     *
     * @param member 비밀번호가 재설정된 멤버 정보
     * @param newPassword 새 비밀번호
     */
    public void sendNewPasswordEmail(Member member, String newPassword) {
        EmailTemplateProperties.Template template = emailTemplateProperties.getTemplate(EmailTemplateType.NEW_PASSWORD);

        String subject = template.getSubject();
        String content = template.getContent()
                .replace("{{id}}", member.getId())
                .replace("{{password}}", newPassword);

        EmailDto emailDto = mapper.of(
                List.of(member.getEmail()),
                subject,
                content,
                EmailTemplateType.NEW_PASSWORD
        );

        sendEmail(emailDto, member, " 비밀번호 재설정 안내 메일 전송에 실패했습니다.");
    }

    /**
     * 이메일 전송을 위한 정보를 기반으로 이메일을 생성하여 비동기 전송합니다.
     *
     * @param emailDto 이메일 전송 정보 객체
     * @param member 전송 대상 멤버
     * @param message 예외 발생 시 로깅될 메시지
     */
    private void sendEmail(EmailDto emailDto, Member member, String message) {
        try {
            String emailContent = generateEmailContent(emailDto, member.getName());
            emailAsyncService.sendEmailAsync(member.getEmail(), emailDto.getSubject(), emailContent, null, emailDto.getEmailTemplateType());
        } catch (MessagingException e) {
            throw new MessageSendingFailedException(member.getEmail() + message);
        }
    }

    /**
     * 이메일 템플릿을 사용하여 이메일 콘텐츠를 생성합니다.
     *
     * @param emailDto 이메일 정보를 포함한 객체
     * @param name 수신자의 이름
     * @return 생성된 이메일 콘텐츠
     */
    private String generateEmailContent(EmailDto emailDto, String name) {
        Context context = new Context();
        context.setVariable("title", emailDto.getSubject());
        context.setVariable("name", name);
        context.setVariable("content", emailDto.getContent());

        String emailTemplate = emailDto.getEmailTemplateType().getTemplateName();
        return springTemplateEngine.process(emailTemplate, context);
    }
}
