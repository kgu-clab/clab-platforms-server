package page.clab.api.domain.members.product.adapter.out.persistence;

import org.mapstruct.Mapper;
import page.clab.api.domain.members.product.domain.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductJpaEntity toJpaEntity(Product product);

    Product toDomainEntity(ProductJpaEntity jpaEntity);
}
