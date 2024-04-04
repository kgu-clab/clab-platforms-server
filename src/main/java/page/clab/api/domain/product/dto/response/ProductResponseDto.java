package page.clab.api.domain.product.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import page.clab.api.domain.product.domain.Product;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ProductResponseDto {

    private Long id;

    private String name;

    private String description;

    private String url;

    private LocalDateTime createdAt;

    public static ProductResponseDto toDto(Product product) {
        return ProductResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .url(product.getUrl())
                .createdAt(product.getCreatedAt())
                .build();
    }

}
