package page.clab.api.domain.login.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import page.clab.api.global.common.domain.BaseEntity;

@Entity
@Table(name = "authenticator")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthenticatorJpaEntity extends BaseEntity {

    @Id
    @Column(name = "member_id", nullable = false)
    private String memberId;

    @Column(nullable = false)
    private String secretKey;
}
