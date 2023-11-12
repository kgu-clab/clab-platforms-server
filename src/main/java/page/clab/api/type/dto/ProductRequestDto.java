package page.clab.api.type.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "서비스명", example = "petmily-server", required = true)
    private String name;

    @NotNull(message = "{notNull.product.description}")
    @Size(min = 1, max = 1000, message = "{size.product.description}")
    @Schema(description = "설명", example = "펫밀리 (Back) - 제10회 소프트웨어 개발보안 시큐어코딩 해커톤", required = true)
    private String description;

    @URL(message = "{url.product.url}")
    @Schema(description = "URL", example = "https://github.com/KGU-C-Lab/petmily-server")
    private String url;

}
