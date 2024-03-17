package page.clab.api.global.common.email.application;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import page.clab.api.domain.member.application.MemberService;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.member.dto.response.MemberResponseDto;
import page.clab.api.global.common.email.domain.EmailTemplateType;
import page.clab.api.global.common.email.dto.request.EmailDto;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender javaMailSender;

    private final MemberService memberService;

    private final SpringTemplateEngine springTemplateEngine;

    @Value("${spring.mail.username}")
    private String sender;

    @Value("${resource.file.path}")
    private String filePath;

    private static final int MAX_BATCH_SIZE = 10;

    private static final BlockingQueue<EmailTask> emailQueue = new LinkedBlockingQueue<>();

    public void broadcastEmail(EmailDto emailDto, List<MultipartFile> multipartFiles) {
        List<File> convertedFiles;
        if(multipartFiles != null && !multipartFiles.isEmpty()) {
            convertedFiles = convertMultipartFiles(multipartFiles);
        } else {
            convertedFiles = null;
        }

        emailDto.getTo().parallelStream().forEach(address -> {
            try {
                Member recipient = memberService.getMemberByEmail(address);
                String emailContent = generateEmailContent(emailDto, recipient.getName());
                sendEmailAsync(address, emailDto.getSubject(), emailContent, convertedFiles, emailDto.getEmailTemplateType());
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void broadcastEmailToAllMember(EmailDto emailDto, List<MultipartFile> multipartFiles) {
        List<File> convertedFiles;
        if(multipartFiles != null && !multipartFiles.isEmpty()) {
            convertedFiles = convertMultipartFiles(multipartFiles);
        } else {
            convertedFiles = null;
        }

        List<MemberResponseDto> memberList = memberService.getMembers();
        memberList.parallelStream().forEach(member -> {
            try {
                String emailContent = generateEmailContent(emailDto, member.getName());
                sendEmailAsync(member.getEmail(), emailDto.getSubject(), emailContent, convertedFiles, emailDto.getEmailTemplateType());
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void broadcastEmailToApprovedMember(Member member, String password) {
        String subject = "C-Lab 계정 발급 안내";
        String content = String.format(
                "정식으로 C-Lab의 일원이 된 것을 축하드립니다.\n" +
                        "C-Lab과 함께하는 동안 불타는 열정으로 모든 원하는 목표를 이루어 내시기를 바라고,\n" +
                        "훗날, 당신의 합류가 C-Lab에겐 최고의 행운이었다고 기억되기를 희망합니다.\n\n" +
                        "로그인을 위해 아래의 계정 정보를 확인해주세요.\n" +
                        "ID: %s\n" +
                        "Password: [여기에 비밀번호 또는 비밀번호 관련 안내를 삽입]\n" +
                        "로그인 후 비밀번호를 변경해주세요.",
                member.getId(),
                password
        );

        EmailDto emailDto = EmailDto.builder()
                .to(List.of(member.getEmail()))
                .subject(subject)
                .content(content)
                .emailTemplateType(EmailTemplateType.NORMAL)
                .build();

        try {
            String emailContent = generateEmailContent(emailDto, member.getName());
            sendEmailAsync(member.getEmail(), emailDto.getSubject(), emailContent, null, emailDto.getEmailTemplateType());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendPasswordResetEmail(Member member, String code) {
        String subject = "C-Lab 비밀번호 재발급 인증 안내";
        String content = String.format(
                "C-Lab 비밀번호 재발급 인증 안내 메일입니다.\n" +
                        "인증번호는 %s입니다.\n" +
                        "해당 인증번호를 비밀번호 재설정 페이지에 입력해주세요.\n" +
                        "재설정시 비밀번호는 인증번호로 대체됩니다.",
                code
        );

        EmailDto emailDto = EmailDto.builder()
                .to(List.of(member.getEmail()))
                .subject(subject)
                .content(content)
                .emailTemplateType(EmailTemplateType.NORMAL)
                .build();

        try {
            broadcastEmail(emailDto, null);
        } catch (Exception e) {
            throw new RuntimeException("비밀번호 재발급 인증 메일 발송에 실패했습니다.", e);
        }
    }

    private List<File> convertMultipartFiles(List<MultipartFile> multipartFiles) {
        List<File> convertedFiles = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            File file = convertMultipartFileToFile(multipartFile);
            convertedFiles.add(file);
        }
        return convertedFiles;
    }

    private File convertMultipartFileToFile(MultipartFile multipartFile) {
        String originalFilename = multipartFile.getOriginalFilename();
        String extension = FilenameUtils.getExtension(originalFilename);
        String path = filePath + File.separator + "temp" + File.separator + System.nanoTime() + "_" + UUID.randomUUID() + "." + extension;
        path = path.replace("/", File.separator).replace("\\", File.separator);
        File file = new File(path);
        checkDir(file);

        try {
            multipartFile.transferTo(file);
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert MultipartFile to File", e);
        }
        return file;
    }

    private String generateEmailContent(EmailDto emailDto, String name){
        Context context = new Context();
        context.setVariable("title", emailDto.getSubject());
        context.setVariable("name", name);
        context.setVariable("content", emailDto.getContent());

        String emailTemplate = emailDto.getEmailTemplateType().getTemplateName();
        return springTemplateEngine.process(emailTemplate, context);
    }

    @Async
    public void sendEmailAsync(String to, String subject, String content, List<File> files, EmailTemplateType emailTemplateType) throws MessagingException {
        log.debug("Sending email to: {}", to);
        emailQueue.add(new EmailTask(to, subject, content, files, emailTemplateType));
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

    protected static class EmailTask {
        private final String to;
        private final String subject;
        private final String content;
        private final List<File> files;
        private final EmailTemplateType templateType;

        public EmailTask(String to, String subject, String content, List<File> files, EmailTemplateType templateType) {
            this.to = to;
            this.subject = subject;
            this.content = content;
            this.files = files;
            this.templateType = templateType;
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

        public List<File> getFiles() {
            return files;
        }

        public EmailTemplateType getTemplateType(){
            return templateType;
        }
    }

    private void setImageInTemplate(MimeMessageHelper messageHelper, EmailTemplateType templateType) throws MessagingException {
        switch(templateType){
            case NORMAL -> {
                messageHelper.addInline("image-1", new ClassPathResource("images/image-1.png"));
                break;
            }
        }
    }

    private void checkDir(File file){
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
    }

}
