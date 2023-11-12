package page.clab.api.type.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenDto {

    @NotNull(message = "{notNull.token.token}")
    @Size(min = 1, message = "{size.token.token}")
    @Schema(description = "엑세스 토큰", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjbGFic2VyIiwiaWF...", required = true)
    private String token;

}
