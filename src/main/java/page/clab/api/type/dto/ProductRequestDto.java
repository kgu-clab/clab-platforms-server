package page.clab.api.type.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
public class ProductRequestDto {

    @NotNull(message = "{notNull.product.name}")
    @Size(min = 1, message = "{size.product.name}")
    private String name;

    @NotNull(message = "{notNull.product.description}")
    @Size(min = 1, max = 1000, message = "{size.product.description}")
    private String description;

    @URL(message = "{url.product.url}")
    private String url;

}
