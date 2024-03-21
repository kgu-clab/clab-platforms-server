package page.clab.api.domain.blacklistIp.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BlacklistIp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String ipAddress;

    private String reason;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    private BlacklistIp(String ipAddress, String reason) {
        this.ipAddress = ipAddress;
        this.reason = reason;
    }

    public static BlacklistIp create(String ipAddress, String reason) {
        return new BlacklistIp(ipAddress, reason);
    }

}