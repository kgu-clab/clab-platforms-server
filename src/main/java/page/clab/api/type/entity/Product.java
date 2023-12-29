package page.clab.api.type.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.URL;
import page.clab.api.type.dto.ProductRequestDto;
import page.clab.api.util.ModelMapperUtil;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(min = 1, message = "{size.product.name}")
    private String name;

    @Column(nullable = false, length = 1000)
    @Size(min = 1, max = 1000, message = "{size.product.description}")
    private String description;

    @URL(message = "{url.product.url}")
    private String url;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public static Product of(ProductRequestDto productRequestDto) {
        return ModelMapperUtil.getModelMapper().map(productRequestDto, Product.class);
    }

}
