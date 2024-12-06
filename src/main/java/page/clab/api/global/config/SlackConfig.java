package page.clab.api.global.config;

import com.slack.api.Slack;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "slack")
public class SlackConfig {

    private String coreTeamWebhookUrl;
    private String executivesWebhookUrl;
    private String webUrl;
    private String apiUrl;
    private String color;

    @Bean
    public Slack slack() {
        return Slack.getInstance();
    }
}
