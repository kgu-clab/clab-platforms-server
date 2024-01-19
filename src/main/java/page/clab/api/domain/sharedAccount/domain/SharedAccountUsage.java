package page.clab.api.domain.sharedAccount.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Version;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import page.clab.api.domain.sharedAccount.dto.request.SharedAccountUsageRequestDto;
import page.clab.api.global.util.ModelMapperUtil;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

    public static SharedAccountUsage of(SharedAccountUsageRequestDto sharedAccountUsageRequestDto) {
        return ModelMapperUtil.getModelMapper().map(sharedAccountUsageRequestDto, SharedAccountUsage.class);
    }

}
