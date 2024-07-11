package page.clab.api.domain.login.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.login.domain.LoginAttemptResult;
import page.clab.api.global.common.domain.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "login_attempt_log")
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginAttemptLogJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private String memberId;
    private String userAgent;
    private String ipAddress;
    private String location;

    @Enumerated(EnumType.STRING)
    private LoginAttemptResult loginAttemptResult;

    private LocalDateTime loginAttemptTime;
}
