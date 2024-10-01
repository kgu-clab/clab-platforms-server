package page.clab.api.global.common.email.application;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import page.clab.api.domain.memberManagement.member.domain.Member;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.global.common.email.domain.EmailTemplateType;
import page.clab.api.global.common.email.dto.request.EmailDto;
import page.clab.api.global.common.email.exception.MessageSendingFailedException;
import page.clab.api.global.config.EmailTemplateProperties;
import page.clab.api.global.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;
    private final SpringTemplateEngine springTemplateEngine;
    private final EmailAsyncService emailAsyncService;
    private final EmailTemplateProperties emailTemplateProperties;

    @Value("${resource.file.path}")
    private String filePath;

    public List<String> broadcastEmail(EmailDto emailDto, List<MultipartFile> multipartFiles) {
        List<File> convertedFiles = multipartFiles != null && !multipartFiles.isEmpty()
                ? convertMultipartFiles(multipartFiles)
                : new ArrayList<>();

        List<String> successfulAddresses = Collections.synchronizedList(new ArrayList<>());

        emailDto.getTo().parallelStream().forEach(address -> {
            try {
                Member recipient = externalRetrieveMemberUseCase.findByEmail(address);
                String emailContent = generateEmailContent(emailDto, recipient.getName());
                emailAsyncService.sendEmailAsync(address, emailDto.getSubject(), emailContent, convertedFiles, emailDto.getEmailTemplateType());
                successfulAddresses.add(address);
            } catch (MessagingException e) {
                throw new MessageSendingFailedException(address + "에게 이메일을 보내는데 실패했습니다.");
            }
        });
        return successfulAddresses;
    }

    public void sendAccountCreationEmail(Member member, String password) {
        EmailTemplateProperties.Template template = emailTemplateProperties.getTemplate(EmailTemplateType.ACCOUNT_CREATION);

        String subject = template.getSubject();
        String content = template.getContent()
                .replace("{{id}}", member.getId())
                .replace("{{password}}", password);

        EmailDto emailDto = EmailDto.create(
                List.of(member.getEmail()),
                subject,
                content,
                EmailTemplateType.ACCOUNT_CREATION
        );

        try {
            String emailContent = generateEmailContent(emailDto, member.getName());
            emailAsyncService.sendEmailAsync(member.getEmail(), emailDto.getSubject(), emailContent, null, emailDto.getEmailTemplateType());
        } catch (MessagingException e) {
            throw new MessageSendingFailedException(member.getEmail() + " 계정 발급 안내 메일 전송에 실패했습니다.");
        }
    }

    public void sendPasswordResetCodeEmail(Member member, String code) {
        EmailTemplateProperties.Template template = emailTemplateProperties.getTemplate(EmailTemplateType.PASSWORD_RESET_CODE);

        String subject = template.getSubject();
        String content = template.getContent()
                .replace("{{code}}", code);

        EmailDto emailDto = EmailDto.create(
                List.of(member.getEmail()),
                subject,
                content,
                EmailTemplateType.PASSWORD_RESET_CODE
        );

        try {
            String emailContent = generateEmailContent(emailDto, member.getName());
            emailAsyncService.sendEmailAsync(member.getEmail(), emailDto.getSubject(), emailContent, null, emailDto.getEmailTemplateType());
        } catch (Exception e) {
            throw new MessageSendingFailedException(member.getEmail() + " 비밀번호 재발급 인증 메일 전송에 실패했습니다.");
        }
    }

    public void sendNewPasswordEmail(Member member, String newPassword) {
        EmailTemplateProperties.Template template = emailTemplateProperties.getTemplate(EmailTemplateType.NEW_PASSWORD);
        String content = template.getContent()
                .replace("{{id}}", member.getId())
                .replace("{{password}}", newPassword);

        EmailDto emailDto = EmailDto.create(List.of(member.getEmail()), template.getSubject(), content, EmailTemplateType.NEW_PASSWORD);
        try {
            String emailContent = generateEmailContent(emailDto, member.getName());
            emailAsyncService.sendEmailAsync(member.getEmail(), emailDto.getSubject(), emailContent, null, emailDto.getEmailTemplateType());
        } catch (MessagingException e) {
            throw new MessageSendingFailedException(member.getEmail() + " 비밀번호 재설정 안내 메일 전송에 실패했습니다.");
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
        String path = filePath + File.separator + "temp" + File.separator + FileUtil.makeFileName(extension);
        path = path.replace("/", File.separator).replace("\\", File.separator);
        File file = new File(path);
        FileUtil.ensureParentDirectoryExists(file, filePath);

        try {
            multipartFile.transferTo(file);
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert MultipartFile to File", e);
        }
        return file;
    }

    private String generateEmailContent(EmailDto emailDto, String name) {
        Context context = new Context();
        context.setVariable("title", emailDto.getSubject());
        context.setVariable("name", name);
        context.setVariable("content", emailDto.getContent());

        String emailTemplate = emailDto.getEmailTemplateType().getTemplateName();
        return springTemplateEngine.process(emailTemplate, context);
    }
}
