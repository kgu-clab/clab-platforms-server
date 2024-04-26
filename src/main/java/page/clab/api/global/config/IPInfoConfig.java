package page.clab.api.global.config;

import io.ipinfo.api.IPinfo;
import io.ipinfo.spring.IPinfoSpring;
import io.ipinfo.spring.strategies.attribute.AttributeStrategy;
import io.ipinfo.spring.strategies.attribute.SessionAttributeStrategy;
import io.ipinfo.spring.strategies.interceptor.BotInterceptorStrategy;
import io.ipinfo.spring.strategies.ip.XForwardedForIPStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IPInfoConfig {

    @Value("${ipinfo.access-token}")
    private static String accessToken;

    @Bean
    public IPinfoSpring ipinfoSpring() {
        return new IPinfoSpring.Builder()
                .setIPinfo(new IPinfo.Builder().setToken(accessToken).build())
                .interceptorStrategy(new BotInterceptorStrategy())
                .ipStrategy(new XForwardedForIPStrategy())
                .attributeStrategy(new SessionAttributeStrategy())
                .build();
    }

    @Bean
    public AttributeStrategy attributeStrategy() {
        return new SessionAttributeStrategy();
    }

    public String getAccessToken() {
        return accessToken;
    }

}
