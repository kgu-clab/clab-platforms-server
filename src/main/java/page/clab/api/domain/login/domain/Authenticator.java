package page.clab.api.domain.login.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Authenticator {

    @Id
    private String memberId;

    @Column(nullable = false)
    private String secretKey;

    private Authenticator(String memberId, String secretKey) {
        this.memberId = memberId;
        this.secretKey = secretKey;
    }

    public static Authenticator create(String memberId, String secretKey) {
        return new Authenticator(memberId, secretKey);
    }

}
