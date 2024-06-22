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
import page.clab.api.domain.member.application.MemberLookupService;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.member.dto.response.MemberResponseDto;
import page.clab.api.global.common.email.domain.EmailTemplateType;
import page.clab.api.global.common.email.dto.request.EmailDto;
import page.clab.api.global.common.email.exception.MessageSendingFailedException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final MemberLookupService memberLookupService;

    private final SpringTemplateEngine springTemplateEngine;

    private final EmailAsyncService emailAsyncService;

    @Value("${resource.file.path}")
    private String filePath;

    public List<String> broadcastEmail(EmailDto emailDto, List<MultipartFile> multipartFiles) {
        List<File> convertedFiles = multipartFiles != null && !multipartFiles.isEmpty()
                ? convertMultipartFiles(multipartFiles)
                : new ArrayList<>();

        List<String> successfulAddresses = Collections.synchronizedList(new ArrayList<>());

        emailDto.getTo().parallelStream().forEach(address -> {
            try {
                Member recipient = memberLookupService.getMemberByEmail(address);
                String emailContent = generateEmailContent(emailDto, recipient.getName());
                emailAsyncService.sendEmailAsync(address, emailDto.getSubject(), emailContent, convertedFiles, emailDto.getEmailTemplateType());
                successfulAddresses.add(address);
            } catch (MessagingException e) {
                throw new MessageSendingFailedException(address + "에게 이메일을 보내는데 실패했습니다.");
            }
        });
        return successfulAddresses;
    }

    public List<String> broadcastEmailToAllMember(EmailDto emailDto, List<MultipartFile> multipartFiles) {
        List<File> convertedFiles = multipartFiles != null && !multipartFiles.isEmpty() ?
                convertMultipartFiles(multipartFiles) : null;

        List<MemberResponseDto> memberList = memberLookupService.getMembers();

        List<String> successfulEmails = Collections.synchronizedList(new ArrayList<>());

        memberList.parallelStream().forEach(member -> {
            try {
                String emailContent = generateEmailContent(emailDto, member.getName());
                emailAsyncService.sendEmailAsync(member.getEmail(), emailDto.getSubject(), emailContent, convertedFiles, emailDto.getEmailTemplateType());
                successfulEmails.add(member.getEmail());
            } catch (MessagingException e) {
                throw new MessageSendingFailedException(member.getEmail() + "에게 이메일을 보내는데 실패했습니다.");
            }
        });
        return successfulEmails;
    }

    public void broadcastEmailToApprovedMember(Member member, String password) {
        String subject = "C-Lab 계정 발급 안내";
        String content = String.format(
                "정식으로 C-Lab의 일원이 된 것을 축하드립니다.\n" +
                        "C-Lab과 함께하는 동안 불타는 열정으로 모든 원하는 목표를 이루어 내시기를 바라고,\n" +
                        "훗날, 당신의 합류가 C-Lab에겐 최고의 행운이었다고 기억되기를 희망합니다.\n\n" +
                        "로그인을 위해 아래의 계정 정보를 확인해주세요.\n" +
                        "ID: %s\n" +
                        "Password: %s\n" +
                        "로그인 후 비밀번호를 변경해주세요.",
                member.getId(),
                password
        );
        EmailDto emailDto = EmailDto.create(List.of(member.getEmail()), subject, content, EmailTemplateType.NORMAL);
        try {
            String emailContent = generateEmailContent(emailDto, member.getName());
            emailAsyncService.sendEmailAsync(member.getEmail(), emailDto.getSubject(), emailContent, null, emailDto.getEmailTemplateType());
        } catch (MessagingException e) {
            throw new MessageSendingFailedException(member.getEmail() + " 계정 발급 안내 메일 전송에 실패했습니다.");
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
        EmailDto emailDto = EmailDto.create(List.of(member.getEmail()), subject, content, EmailTemplateType.NORMAL);
        try {
            broadcastEmail(emailDto, null);
        } catch (Exception e) {
            throw new MessageSendingFailedException(member.getEmail() + " 비밀번호 재발급 인증 메일 전송에 실패했습니다.");
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

    private String generateEmailContent(EmailDto emailDto, String name) {
        Context context = new Context();
        context.setVariable("title", emailDto.getSubject());
        context.setVariable("name", name);
        context.setVariable("content", emailDto.getContent());

        String emailTemplate = emailDto.getEmailTemplateType().getTemplateName();
        return springTemplateEngine.process(emailTemplate, context);
    }

    private void checkDir(File file) {
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
    }

}
