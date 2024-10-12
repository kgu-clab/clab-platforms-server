package page.clab.api.domain.members.product.application.dto.mapper;

import org.springframework.stereotype.Component;
import page.clab.api.domain.members.product.application.dto.request.ProductRequestDto;
import page.clab.api.domain.members.product.application.dto.response.ProductResponseDto;
import page.clab.api.domain.members.product.domain.Product;

@Component
public class ProductDtoMapper {

    public Product fromDto(ProductRequestDto requestDto) {
        return Product.builder()
                .name(requestDto.getName())
                .description(requestDto.getDescription())
                .url(requestDto.getUrl())
                .isDeleted(false)
                .build();
    }

    public ProductResponseDto toDto(Product product) {
        return ProductResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .url(product.getUrl())
                .createdAt(product.getCreatedAt())
                .build();
    }
}
