package page.clab.api.service;

import java.util.List;
import java.util.Objects;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import page.clab.api.type.dto.EmailDto;
import page.clab.api.type.dto.MemberResponseDto;

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

    public void broadcastEmail(EmailDto emailDto, List<MultipartFile> files) {
        emailDto.getTo().parallelStream().forEach(member -> {
            try {
                sendEmail(member, emailDto.getSubject(), emailDto.getContent(), files);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void broadcastEmailToAllMember(EmailDto emailDto, List<MultipartFile> files) {
        List<MemberResponseDto> memberList = memberService.getMembers();
         memberList.parallelStream().forEach(member -> {
             try {
                 sendEmail(member.getEmail(), emailDto.getSubject(), emailDto.getContent(), files);
             } catch (MessagingException e) {
                 throw new RuntimeException(e);
             }
        });
    }

}
