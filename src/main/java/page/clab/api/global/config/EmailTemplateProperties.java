package page.clab.api.global.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import page.clab.api.global.common.email.domain.EmailTemplateType;

import java.util.Map;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "email")
public class EmailTemplateProperties {

    private Map<String, Template> templates;

    public Template getTemplate(EmailTemplateType templateType) {
        Template template = templates.get(templateType.getKey());
        if (template == null) {
            throw new IllegalArgumentException("템플릿을 찾을 수 없습니다: " + templateType);
        }
        return template;
    }

    @Getter
    @Setter
    public static class Template {
        private String subject;
        private String content;
    }
}
