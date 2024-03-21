package page.clab.api.global.common.verificationCode.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;
import page.clab.api.global.exception.InvalidInformationException;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "verification-code", timeToLive = 60*3)
public class VerificationCode {

    @Id
    @Column(name = "member_id", nullable = false)
    @Size(min = 9, max = 9, message = "{size.verificationCode.memberId}")
    private String id;

    @Indexed
    @Size(min = 12, max = 12, message = "{size.verificationCode.verificationCode}")
    @Column(nullable = false)
    private String verificationCode;

    public static VerificationCode create(String memberId, String verificationCode) {
        return VerificationCode.builder()
                .id(memberId)
                .verificationCode(verificationCode)
                .build();
    }

    public boolean isOwner(String memberId) {
        return this.id.equals(memberId);
    }

    public void validateRequest(String memberId) {
        if (!isOwner(memberId)) {
            throw new InvalidInformationException("올바르지 않은 인증 요청입니다.");
        }
    }

}