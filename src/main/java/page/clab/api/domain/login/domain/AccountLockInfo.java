package page.clab.api.domain.login.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.member.domain.Member;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountLockInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private Long loginFailCount;

    private Boolean isLock;

    private LocalDateTime lockUntil;

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
        if (isLock != null && isLock) {
            return lockUntil.isAfter(LocalDateTime.now());
        }
        return false;
    }

    public void unlockAccount() {
        this.isLock = false;
        this.loginFailCount = 0L;
        this.lockUntil = null;
    }

}

