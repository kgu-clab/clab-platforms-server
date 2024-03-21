package page.clab.api.domain.product.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;
import page.clab.api.domain.product.dto.request.ProductRequestDto;
import page.clab.api.domain.product.dto.request.ProductUpdateRequestDto;
import page.clab.api.global.common.domain.BaseEntity;
import page.clab.api.global.util.ModelMapperUtil;

import java.util.Optional;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(min = 1, message = "{size.product.name}")
    private String name;

    @Column(nullable = false)
    @Size(min = 1, max = 1000, message = "{size.product.description}")
    private String description;

    @URL(message = "{url.product.url}")
    private String url;

    public static Product of(ProductRequestDto productRequestDto) {
        return ModelMapperUtil.getModelMapper().map(productRequestDto, Product.class);
    }

    public void update(ProductUpdateRequestDto productUpdateRequestDto) {
        Optional.ofNullable(productUpdateRequestDto.getName()).ifPresent(this::setName);
        Optional.ofNullable(productUpdateRequestDto.getDescription()).ifPresent(this::setDescription);
        Optional.ofNullable(productUpdateRequestDto.getUrl()).ifPresent(this::setUrl);
    }

}
