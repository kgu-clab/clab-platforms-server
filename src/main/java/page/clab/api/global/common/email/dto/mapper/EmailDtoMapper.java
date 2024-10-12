package page.clab.api.global.common.email.dto.mapper;

import org.springframework.stereotype.Component;
import page.clab.api.global.common.email.domain.EmailTemplateType;
import page.clab.api.global.common.email.dto.request.EmailDto;

import java.util.List;

@Component
public class EmailDtoMapper {

    public EmailDto of(List<String> to, String subject, String content, EmailTemplateType emailTemplateType) {
        return EmailDto.builder()
                .to(to)
                .subject(subject)
                .content(content)
                .emailTemplateType(emailTemplateType)
                .build();
    }
}
