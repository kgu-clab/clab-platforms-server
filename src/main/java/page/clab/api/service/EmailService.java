package page.clab.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import page.clab.api.type.dto.EmailDto;
import page.clab.api.type.dto.MemberResponseDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender javaMailSender;

    private final MemberService memberService;

    @Value("${spring.mail.username}")
    private String sender;

    private static final int MAX_BATCH_SIZE = 10;

    private static final BlockingQueue<EmailTask> emailQueue = new LinkedBlockingQueue<>();


    public void broadcastEmail(EmailDto emailDto, List<MultipartFile> files) {
        emailDto.getTo().parallelStream().forEach(member -> {
            try {
                sendEmailAsync(member, emailDto.getSubject(), emailDto.getContent(), files);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void broadcastEmailToAllMember(EmailDto emailDto, List<MultipartFile> files) {
        List<MemberResponseDto> memberList = memberService.getMembers();
        memberList.parallelStream().forEach(member -> {
            try {
                sendEmailAsync(member.getEmail(), emailDto.getSubject(), emailDto.getContent(), files);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Async
    public void sendEmailAsync(String to, String subject, String content, List<MultipartFile> files) throws MessagingException {
        log.debug("Sending email to: {}", to);
        emailQueue.add(new EmailTask(to, subject, content, files));
    }

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

    public void sendBatchEmail(List<EmailTask> emailTasks) throws MessagingException {
        MimeMessage[] mimeMessages = new MimeMessage[emailTasks.size()];
        for (int i = 0; i < emailTasks.size(); i++) {
            EmailTask task = emailTasks.get(i);
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
            messageHelper.setFrom(sender);
            messageHelper.setTo(task.getTo());
            messageHelper.setSubject(task.getSubject());
            messageHelper.setText(task.getContent());
            if (task.getFiles() != null) {
                for (MultipartFile file : task.getFiles()) {
                    messageHelper.addAttachment(Objects.requireNonNull(file.getOriginalFilename()), file);
                }
            }
            mimeMessages[i] = message;
        }
        javaMailSender.send(mimeMessages);
        log.debug("Batch email sent successfully.");
    }

    protected static class EmailTask {
        private final String to;
        private final String subject;
        private final String content;
        private final List<MultipartFile> files;

        public EmailTask(String to, String subject, String content, List<MultipartFile> files) {
            this.to = to;
            this.subject = subject;
            this.content = content;
            this.files = files;
        }

        public String getTo() {
            return to;
        }

        public String getSubject() {
            return subject;
        }

        public String getContent() {
            return content;
        }

        public List<MultipartFile> getFiles() {
            return files;
        }
    }

}
