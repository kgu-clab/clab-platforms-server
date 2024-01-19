package page.clab.api.domain.product.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.product.domain.Product;
import page.clab.api.global.util.ModelMapperUtil;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponseDto {

    private Long id;

    private String name;

    private String description;

    private String url;

    private LocalDateTime createdAt;

    public static ProductResponseDto of(Product product) {
        return ModelMapperUtil.getModelMapper().map(product, ProductResponseDto.class);
    }

}
