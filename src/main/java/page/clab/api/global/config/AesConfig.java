package page.clab.api.global.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import page.clab.api.global.util.EncryptionUtil;

@Getter
@Component
public class AesConfig {

    @Value("${security.aes-key}")
    private String secretKey;

    @PostConstruct
    public void init() {
        EncryptionUtil.setSecretKey(secretKey);
    }

}
