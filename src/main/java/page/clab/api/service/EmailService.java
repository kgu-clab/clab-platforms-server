package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import page.clab.api.type.dto.EmailDto;
import page.clab.api.type.dto.MemberResponseDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    public void sendEmail(String to, EmailDto emailDto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("gksrhksgml11@gmail.com");
        message.setTo(to);
        message.setSubject(emailDto.getTitle());
        message.setText(emailDto.getContent());
        javaMailSender.send(message);
    }

    public void broadcastEmailToAllMember(List<MemberResponseDto> memberList, EmailDto emailDto) {
        for (MemberResponseDto member : memberList) {
            sendEmail(member.getEmail(), emailDto);
        }
    }

}
