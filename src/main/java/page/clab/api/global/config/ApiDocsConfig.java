package page.clab.api.global.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
public class ApiDocsConfig {

    @Value("${swagger.endpoint}")
    private String swaggerEndpoint;
}
