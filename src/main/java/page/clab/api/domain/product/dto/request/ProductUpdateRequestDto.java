package page.clab.api.domain.product.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
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
public class ProductUpdateRequestDto {

    @Size(min = 1, message = "{size.product.name}")
    @Schema(description = "서비스명", example = "petmily-server")
    private String name;

    @Size(min = 1, max = 1000, message = "{size.product.description}")
    @Schema(description = "설명", example = "펫밀리 (Back) - 제10회 소프트웨어 개발보안 시큐어코딩 해커톤")
    private String description;

    @URL(message = "{url.product.url}")
    @Schema(description = "URL", example = "https://github.com/KGU-C-Lab/petmily-server")
    private String url;

}
