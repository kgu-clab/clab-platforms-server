package page.clab.api.global.common.email.application;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeUtility;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import page.clab.api.global.common.email.domain.EmailTask;
import page.clab.api.global.common.email.domain.EmailTemplateType;
import page.clab.api.global.util.LogSanitizerUtil;

/**
 * {@code EmailAsyncService}는 비동기적으로 이메일을 전송하고 이메일 대기열을 관리하는 서비스 클래스입니다.
 *
 * <p>이 클래스는 Spring의 JavaMailSender를 사용하여 이메일을 전송하며, 대용량 이메일 전송을 위해 배치 처리를 지원합니다.
 * 이메일은 일정 간격으로 대기열에서 가져와 전송됩니다. 또한, 이메일 템플릿에 포함된 이미지를 지원하며 첨부 파일도 추가할 수 있습니다.</p>
 * <p>
 * 주요 기능:
 * <ul>
 *     <li>{@link #sendEmailAsync(String, String, String, List, EmailTemplateType)} - 비동기적으로 이메일을 대기열에 추가합니다.</li>
 *     <li>{@link #processEmailQueue()} - 대기열에 쌓인 이메일을 일정 간격으로 배치 전송합니다.</li>
 *     <li>{@link #sendBatchEmail(List)} - 주어진 배치 내 모든 이메일을 전송합니다.</li>
 *     <li>{@link #setImageInTemplate(MimeMessageHelper, EmailTemplateType)} - 템플릿에 따른 이미지를 추가합니다.</li>
 * </ul>
 *
 * @see JavaMailSender
 * @see MimeMessage
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailAsyncService {

    private static final int MAX_BATCH_SIZE = 10;
    private static final BlockingQueue<EmailTask> emailQueue = new LinkedBlockingQueue<>();
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    /**
     * 비동기적으로 이메일을 대기열에 추가합니다.
     *
     * @param to                수신자 이메일 주소
     * @param subject           이메일 제목
     * @param content           이메일 본문 내용 (HTML 지원)
     * @param files             첨부 파일 목록 (선택 사항)
     * @param emailTemplateType 이메일 템플릿 유형
     * @throws MessagingException 이메일 생성 중 오류 발생 시
     */
    @Async
    public void sendEmailAsync(String to, String subject, String content, List<File> files,
        EmailTemplateType emailTemplateType) throws MessagingException {
        log.debug("Sending email to: {}", LogSanitizerUtil.sanitizeForLog(to));
        emailQueue.add(new EmailTask(to, subject, content, files, emailTemplateType));
    }

    /**
     * 일정 간격으로 이메일 대기열을 확인하고 배치 전송합니다.
     *
     * <p>대기열에서 이메일을 꺼내 배치를 구성하고, 구성된 배치가 일정 크기 이상이면 전송합니다.
     * 남아 있는 이메일이 있으면 즉시 전송합니다.</p>
     */
    @Async
    @Scheduled(fixedRate = 1000)
    public void processEmailQueue() {
        try {
            List<EmailTask> batch = new ArrayList<>();
            EmailTask task;
            while ((task = emailQueue.poll()) != null) {
                batch.add(task);
                if (batch.size() >= MAX_BATCH_SIZE) {
                    sendBatchEmail(batch);
                    batch.clear();
                }
            }
            if (!batch.isEmpty()) {
                sendBatchEmail(batch);
            }
        } catch (Exception e) {
            log.debug("Error processing email queue: {}", e.getMessage(), e);
        }
    }

    /**
     * 주어진 이메일 목록을 배치로 전송합니다.
     *
     * @param emailTasks 전송할 이메일 작업 목록
     * @throws MessagingException 이메일 생성 중 오류 발생 시
     * @throws IOException        첨부 파일 처리 중 오류 발생 시
     */
    public void sendBatchEmail(List<EmailTask> emailTasks) throws MessagingException, IOException {
        MimeMessage[] mimeMessages = new MimeMessage[emailTasks.size()];
        for (int i = 0; i < emailTasks.size(); i++) {
            EmailTask task = emailTasks.get(i);
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
            messageHelper.setFrom(sender);
            messageHelper.setTo(task.getTo());
            messageHelper.setSubject(task.getSubject());
            messageHelper.setText(task.getContent(), true);

            setImageInTemplate(messageHelper, task.getTemplateType());

            if (task.getFiles() != null) {
                for (File file : task.getFiles()) {
                    messageHelper.addAttachment(MimeUtility.encodeText(file.getName(), "UTF-8", "B"), file);
                }
            }
            mimeMessages[i] = message;
        }

        try {
            javaMailSender.send(mimeMessages);
        } catch (Exception e) {
            log.error("Error sending batch email: {}", e.getMessage());
        }
        log.debug("Batch email sent successfully.");
    }

    /**
     * 이메일 템플릿에 맞는 이미지를 추가합니다.
     *
     * @param messageHelper MimeMessageHelper 객체, 이메일 생성에 사용
     * @param templateType  템플릿 유형을 나타내는 {@link EmailTemplateType} 객체
     * @throws MessagingException 이미지 삽입 중 오류 발생 시
     */
    private void setImageInTemplate(MimeMessageHelper messageHelper, EmailTemplateType templateType)
        throws MessagingException {
        if (Objects.equals(templateType.getTemplateName(), "clabEmail.html")) {
            messageHelper.addInline("image-1", new ClassPathResource("images/image-1.png"));
        }
    }
}
