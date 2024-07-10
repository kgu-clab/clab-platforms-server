package page.clab.api.domain.activityGroup.domain;

import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@RedisHash(value = "QRCodeKey", timeToLive = 60 * 3)
public class RedisQRKey {

    @Id
    @Indexed
    @Column(name = "QRCode_key")
    private String QRCodeKey;

    public static RedisQRKey create(String QRCodeKey) {
        return RedisQRKey.builder()
                .QRCodeKey(QRCodeKey)
                .build();
    }
}
