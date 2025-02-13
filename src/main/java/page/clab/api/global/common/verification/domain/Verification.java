package page.clab.api.global.common.verification.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;
import page.clab.api.global.exception.BaseException;
import page.clab.api.global.exception.ErrorCode;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@RedisHash(value = "verification", timeToLive = 60 * 3)
public class Verification {

    @Id
    @Column(name = "member_id", nullable = false)
    @Size(min = 9, max = 9, message = "{size.verificationCode.memberId}")
    private String id;

    @Indexed
    @Size(min = 12, max = 12, message = "{size.verificationCode.verificationCode}")
    @Column(nullable = false)
    private String verificationCode;

    public static Verification create(String memberId, String verificationCode) {
        return Verification.builder()
            .id(memberId)
            .verificationCode(verificationCode)
            .build();
    }

    public boolean isOwner(String memberId) {
        return this.id.equals(memberId);
    }

    public void validateRequest(String memberId) {
        if (!isOwner(memberId)) {
            throw new BaseException(ErrorCode.INVALID_VERIFICATION_REQUEST);
        }
    }
}
