package page.clab.api.global.common.email.application;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import page.clab.api.global.common.email.domain.EmailTask;
import page.clab.api.global.common.email.domain.EmailTemplateType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailAsyncService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    private static final int MAX_BATCH_SIZE = 10;

    private static final BlockingQueue<EmailTask> emailQueue = new LinkedBlockingQueue<>();

    @Async
    public void sendEmailAsync(String to, String subject, String content, List<File> files, EmailTemplateType emailTemplateType) throws MessagingException {
        log.debug("Sending email to: {}", to);
        emailQueue.add(new EmailTask(to, subject, content, files, emailTemplateType));
    }

    @Async
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
            log.error("Error sending batch email: " + e.getMessage(), e);
        }
        log.debug("Batch email sent successfully.");
    }

    private void setImageInTemplate(MimeMessageHelper messageHelper, EmailTemplateType templateType) throws MessagingException {
        switch(templateType) {
            case NORMAL -> {
                messageHelper.addInline("image-1", new ClassPathResource("images/image-1.png"));
                break;
            }
        }
    }

}
