package page.clab.api.type.dto;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SharedAccountRequestDto {

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private String platformName;

    @NotNull
    @URL
    private String platformUrl;

}
