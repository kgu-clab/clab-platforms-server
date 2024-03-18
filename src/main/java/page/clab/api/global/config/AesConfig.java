package page.clab.api.global.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class AesConfig {

    @Value("${security.aes-key}")
    private String secretKey;

    private int ivLengthBytes = 12;

    private int gcmTagLengthBits = 128;

}
