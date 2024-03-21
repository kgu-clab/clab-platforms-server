package page.clab.api.domain.sharedAccount.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.sharedAccount.exception.InvalidUsageTimeException;
import page.clab.api.domain.sharedAccount.exception.SharedAccountUsageStateException;
import page.clab.api.global.common.domain.BaseEntity;
import page.clab.api.global.exception.PermissionDeniedException;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(indexes = {@Index(name = "idx_shared_account_usage", columnList = "status, startTime, endTime")})
public class SharedAccountUsage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "shared_account_id", nullable = false)
    private SharedAccount sharedAccount;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private String memberId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SharedAccountUsageStatus status;

    @Version
    private Long version;

    public static SharedAccountUsage create(SharedAccount sharedAccount, String memberId, LocalDateTime startTime, LocalDateTime endTime) {
        return SharedAccountUsage.builder()
                .sharedAccount(sharedAccount)
                .memberId(memberId)
                .startTime(startTime)
                .endTime(endTime)
                .build();
    }

    public boolean isOwner(Member member) {
        return this.memberId.equals(member.getId());
    }

    public void validateAccessPermission(Member member) throws PermissionDeniedException {
        if (!isOwner(member) && !member.isAdminRole()) {
            throw new PermissionDeniedException("공유 계정 이용 상태 변경 권한이 없습니다.");
        }
    }

    public void validateUsageTimes(LocalDateTime currentTime) {
        if (this.endTime.isBefore(currentTime)) {
            throw new InvalidUsageTimeException("이용 종료 시간은 현재 시간 이후여야 합니다.");
        }
        if (this.endTime.isBefore(this.startTime)) {
            throw new InvalidUsageTimeException("이용 종료 시간은 시작 시간 이후여야 합니다.");
        }
    }

    public void determineStatus(LocalDateTime currentTime) {
        if (!currentTime.isBefore(startTime) && currentTime.isBefore(endTime)) {
            this.status = SharedAccountUsageStatus.IN_USE;
            this.sharedAccount.setInUse(true);
        } else if (currentTime.isAfter(endTime)) {
            this.status = SharedAccountUsageStatus.COMPLETED;
        } else if (currentTime.isBefore(startTime)) {
            this.status = SharedAccountUsageStatus.RESERVED;
        }
    }

    public void updateStatus(SharedAccountUsageStatus newStatus, Member currentMember) throws PermissionDeniedException {
        validateAccessPermission(currentMember);
        switch (newStatus) {
            case CANCELED:
                cancelUsage(currentMember);
                break;
            case COMPLETED:
                completeUsage(currentMember);
                break;
            default:
                throw new SharedAccountUsageStateException("지원하지 않는 상태 업데이트입니다.");
        }
    }

    private void cancelUsage(Member currentMember) {
        if (!isInUsableState()) {
            throw new SharedAccountUsageStateException("IN_USE 또는 RESERVED 상태에서만 취소할 수 있습니다.");
        }
        setStatus(SharedAccountUsageStatus.CANCELED);
        if (SharedAccountUsageStatus.IN_USE.equals(status)) {
            sharedAccount.setInUse(false);
        }
    }

    private void completeUsage(Member currentMember) {
        if (!SharedAccountUsageStatus.IN_USE.equals(status)) {
            throw new SharedAccountUsageStateException("IN_USE 상태에서만 완료할 수 있습니다.");
        }
        setStatus(SharedAccountUsageStatus.COMPLETED);
        sharedAccount.setInUse(false);
    }

    private boolean isInUsableState() {
        return SharedAccountUsageStatus.IN_USE.equals(status) || SharedAccountUsageStatus.RESERVED.equals(status);
    }

}
