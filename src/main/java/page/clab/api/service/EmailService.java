package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import page.clab.api.type.dto.EmailDto;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    public void sendEmail(EmailDto emailDto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("gksrhksgml11@gmail.com");
        message.setTo(emailDto.getTo());
        message.setSubject(emailDto.getTitle());
        message.setText(emailDto.getContent());
        javaMailSender.send(message);
    }
}
