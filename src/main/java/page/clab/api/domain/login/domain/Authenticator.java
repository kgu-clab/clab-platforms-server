package page.clab.api.domain.login.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Authenticator {

    private String memberId;
    private String secretKey;

    public static Authenticator create(String memberId, String secretKey) {
        return Authenticator.builder()
                .memberId(memberId)
                .secretKey(secretKey)
                .build();
    }
}
