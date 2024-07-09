package page.clab.api.domain.login.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.global.common.domain.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountLockInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private String memberId;

    private Long loginFailCount;

    private Boolean isLock;

    private LocalDateTime lockUntil;

    public static AccountLockInfo create(String memberId) {
        return AccountLockInfo.builder()
                .memberId(memberId)
                .loginFailCount(0L)
                .isLock(false)
                .lockUntil(null)
                .build();
    }

    public void banPermanently() {
        this.isLock = true;
        this.lockUntil = LocalDateTime.of(9999, 12, 31, 23, 59);
    }

    public void unban() {
        this.isLock = false;
        this.lockUntil = null;
    }

    public void incrementLoginFailCount() {
        this.loginFailCount += 1;
    }

    public boolean shouldBeLocked(int maxLoginFailures) {
        return this.loginFailCount >= maxLoginFailures;
    }

    public void lockAccount(int lockDurationMinutes) {
        this.isLock = true;
        this.lockUntil = LocalDateTime.now().plusMinutes(lockDurationMinutes);
    }

    public boolean isCurrentlyLocked() {
        updateLockStatus();
        return isLock != null && isLock;
    }

    public void unlockAccount() {
        this.isLock = false;
        this.loginFailCount = 0L;
        this.lockUntil = null;
    }

    private void updateLockStatus() {
        if (isLock != null && isLock && lockUntil.isBefore(LocalDateTime.now())) {
            unlockAccount();
        }
    }
}

