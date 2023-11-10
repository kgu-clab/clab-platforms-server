package page.clab.api.type.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;
import page.clab.api.type.dto.SharedAccountRequestDto;
import page.clab.api.util.ModelMapperUtil;

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

    public static SharedAccount of(SharedAccountRequestDto sharedAccountRequestDto) {
        return ModelMapperUtil.getModelMapper().map(sharedAccountRequestDto, SharedAccount.class);
    }

}
