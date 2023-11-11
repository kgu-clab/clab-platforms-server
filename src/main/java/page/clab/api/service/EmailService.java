package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.type.dto.EmailDto;
import page.clab.api.type.dto.MemberResponseDto;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    private final MemberService memberService;

    public void sendEmail(String to, String subject, String content, List<MultipartFile> files) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);

        messageHelper.setFrom("clab.coreteam@gmail.com");
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);
        messageHelper.setText(content);
        if (files != null) {
            for (MultipartFile file : files) {
                messageHelper.addAttachment(Objects.requireNonNull(file.getOriginalFilename()), file);
            }
        }
        javaMailSender.send(message);
    }

    public void broadcastEmail(EmailDto emailDto) throws MessagingException, PermissionDeniedException {
//        memberService.checkMemberAdminRole();
        for (String member : emailDto.getTo()) {
            sendEmail(member, emailDto.getSubject(), emailDto.getContent(), emailDto.getFiles());
        }
    }

    public void broadcastEmailToAllMember(List<MemberResponseDto> memberList, EmailDto emailDto) throws MessagingException, PermissionDeniedException {
//        memberService.checkMemberAdminRole();
        for (MemberResponseDto member : memberList) {
            sendEmail(member.getEmail(), emailDto.getSubject(), emailDto.getContent(), emailDto.getFiles());
        }
    }

}
