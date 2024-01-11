package page.clab.api.type.entity;

import javax.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "QRCodeKey", timeToLive = 60 * 3)
public class RedisQRKey {

    @Id
    @Column(name = "QRCode_key")
    private String QRCodeKey;

}
