package page.clab.api.global.config;

import io.ipinfo.api.IPinfo;
import io.ipinfo.spring.IPinfoSpring;
import io.ipinfo.spring.strategies.attribute.AttributeStrategy;
import io.ipinfo.spring.strategies.attribute.SessionAttributeStrategy;
import io.ipinfo.spring.strategies.interceptor.BotInterceptorStrategy;
import io.ipinfo.spring.strategies.interceptor.InterceptorStrategy;
import io.ipinfo.spring.strategies.ip.IPStrategy;
import io.ipinfo.spring.strategies.ip.XForwardedForIPStrategy;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Getter
@Configuration
public class IPInfoConfig {

    private final RestClient restClient = RestClient.create("https://ipinfo.io/");

    @Value("${ipinfo.access-token}")
    private String accessToken;

    @Bean
    public IPinfoSpring ipinfoSpring() {
        return new IPinfoSpring.Builder()
                .setIPinfo(ipInfo())
                .interceptorStrategy(interceptorStrategy())
                .ipStrategy(ipStrategy())
                .attributeStrategy(attributeStrategy())
                .build();
    }

    @Bean
    public IPinfo ipInfo() {
        return new IPinfo.Builder().setToken(accessToken).build();
    }

    @Bean
    public InterceptorStrategy interceptorStrategy() {
        return new BotInterceptorStrategy();
    }

    @Bean
    public IPStrategy ipStrategy() {
        return new XForwardedForIPStrategy();
    }

    @Bean
    public AttributeStrategy attributeStrategy() {
        return new SessionAttributeStrategy();
    }

}
