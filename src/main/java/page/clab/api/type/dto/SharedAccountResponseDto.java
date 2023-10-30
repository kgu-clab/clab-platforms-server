package page.clab.api.type.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.type.entity.SharedAccount;
import page.clab.api.util.ModelMapperUtil;

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

    public static SharedAccountResponseDto of(SharedAccount sharedAccount) {
        return ModelMapperUtil.getModelMapper().map(sharedAccount, SharedAccountResponseDto.class);
    }

}
