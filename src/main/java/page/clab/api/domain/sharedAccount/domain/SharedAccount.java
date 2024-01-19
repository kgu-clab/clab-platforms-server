package page.clab.api.domain.sharedAccount.domain;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.URL;
import page.clab.api.domain.sharedAccount.dto.request.SharedAccountRequestDto;
import page.clab.api.global.util.ModelMapperUtil;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SharedAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(min = 1, message = "{size.sharedAccount.username}")
    private String username;

    @Column(nullable = false)
    @Size(min = 1, message = "{size.sharedAccount.password}")
    private String password;

    @Column(nullable = false)
    @Size(min = 1, message = "{size.sharedAccount.platformName}")
    private String platformName;

    @Column(nullable = false)
    @URL(message = "{url.sharedAccount.platformUrl}")
    private String platformUrl;

    @Column(nullable = false)
    private boolean isInUse;
    
    @CreationTimestamp
    private LocalDateTime createdAt;

    public static SharedAccount of(SharedAccountRequestDto sharedAccountRequestDto) {
        return ModelMapperUtil.getModelMapper().map(sharedAccountRequestDto, SharedAccount.class);
    }

}
