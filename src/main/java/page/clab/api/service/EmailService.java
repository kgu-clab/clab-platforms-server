package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import page.clab.api.type.dto.MemberResponseDto;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    public void sendEmail(String to, String subject, String content, List<MultipartFile> files) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);

        messageHelper.setFrom("gksrhksgml11@gmail.com");
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

    public void broadcastEmailToAllMember(List<MemberResponseDto> memberList, String subject, String content, List<MultipartFile> files) throws MessagingException {
        for (MemberResponseDto member : memberList) {
            sendEmail(member.getEmail(), subject, content, files);
        }
    }

    public void broadcastEmailToGroup(List<String> to, String subject, String content, List<MultipartFile> files) throws MessagingException {
        for (String email : to) {
            sendEmail(email, subject, content, files);
        }
    }

}
