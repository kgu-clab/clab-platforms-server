package page.clab.api.domain.sharedAccount.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.sharedAccount.domain.SharedAccount;
import page.clab.api.global.util.ModelMapperUtil;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SharedAccountResponseDto {

    private Long id;

    private String username;

    private String password;

    private String platformName;

    private String platformUrl;

    private boolean isInUse;

    public static SharedAccountResponseDto of(SharedAccount sharedAccount) {
        return ModelMapperUtil.getModelMapper().map(sharedAccount, SharedAccountResponseDto.class);
    }

}
