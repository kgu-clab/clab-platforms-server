package page.clab.api.domain.members.product.adapter.out.persistence;

import org.springframework.stereotype.Component;
import page.clab.api.domain.members.product.domain.Product;

@Component
public class ProductMapper {

    public ProductJpaEntity toJpaEntity(Product product) {
        return ProductJpaEntity.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .url(product.getUrl())
                .isDeleted(product.isDeleted())
                .build();
    }

    public Product toDomainEntity(ProductJpaEntity jpaEntity) {
        return Product.builder()
                .id(jpaEntity.getId())
                .name(jpaEntity.getName())
                .description(jpaEntity.getDescription())
                .url(jpaEntity.getUrl())
                .isDeleted(jpaEntity.isDeleted())
                .createdAt(jpaEntity.getCreatedAt())
                .build();
    }
}
