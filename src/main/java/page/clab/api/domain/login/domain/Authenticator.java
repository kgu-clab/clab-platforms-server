package page.clab.api.domain.login.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import page.clab.api.global.common.domain.BaseEntity;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Authenticator extends BaseEntity {

    @Id
    private String memberId;

    @Column(nullable = false)
    private String secretKey;

    public static Authenticator create(String memberId, String secretKey) {
        return Authenticator.builder()
                .memberId(memberId)
                .secretKey(secretKey)
                .build();
    }
}
