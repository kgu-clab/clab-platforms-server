package page.clab.api.type.entity;

import javax.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "QRCodeKey", timeToLive = 60 * 3)
public class RedisQRKey {

    @Id
    @Indexed
    @Column(name = "QRCode_key")
    private String QRCodeKey;

}
