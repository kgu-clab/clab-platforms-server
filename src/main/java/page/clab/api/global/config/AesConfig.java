package page.clab.api.global.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import page.clab.api.global.util.EncryptionUtil;

@Getter
@Component
public class AesConfig {

    @Value("${security.aes-key}")
    private String secretKey;
    private final int ivLengthBytes = 12;
    private final int gcmTagLengthBits = 128;

    @Bean
    public EncryptionUtil encryptionUtil(AesConfig aesConfig) {
        return EncryptionUtil.create(aesConfig);
    }
}
