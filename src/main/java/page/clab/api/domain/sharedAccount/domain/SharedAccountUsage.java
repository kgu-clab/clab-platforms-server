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
import org.hibernate.annotations.CreationTimestamp;
import page.clab.api.domain.sharedAccount.exception.InvalidUsageTimeException;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(indexes = {@Index(name = "idx_shared_account_usage", columnList = "status, startTime, endTime")})
public class SharedAccountUsage {

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

    @CreationTimestamp
    private LocalDateTime createdAt;

    public static SharedAccountUsage create(SharedAccount sharedAccount, String memberId, LocalDateTime startTime, LocalDateTime endTime) {
        return SharedAccountUsage.builder()
                .sharedAccount(sharedAccount)
                .memberId(memberId)
                .startTime(startTime)
                .endTime(endTime)
                .build();
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


}
