package page.clab.api.domain.sharedAccount.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;
import page.clab.api.domain.sharedAccount.dto.request.SharedAccountUpdateRequestDto;
import page.clab.api.global.common.domain.BaseEntity;

import java.util.Optional;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SharedAccount extends BaseEntity {

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

    public void update(SharedAccountUpdateRequestDto sharedAccountUpdateRequestDto) {
        Optional.ofNullable(sharedAccountUpdateRequestDto.getUsername()).ifPresent(this::setUsername);
        Optional.ofNullable(sharedAccountUpdateRequestDto.getPassword()).ifPresent(this::setPassword);
        Optional.ofNullable(sharedAccountUpdateRequestDto.getPlatformName()).ifPresent(this::setPlatformName);
        Optional.ofNullable(sharedAccountUpdateRequestDto.getPlatformUrl()).ifPresent(this::setPlatformUrl);
    }

    public void updateIsInUse(boolean isInUse) {
        this.isInUse = isInUse;
    }

}
