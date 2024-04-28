package page.clab.api.domain.product.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.validator.constraints.URL;
import page.clab.api.domain.product.dto.request.ProductUpdateRequestDto;
import page.clab.api.global.common.domain.BaseEntity;

import java.util.Optional;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE product SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
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

    public void update(ProductUpdateRequestDto productUpdateRequestDto) {
        Optional.ofNullable(productUpdateRequestDto.getName()).ifPresent(this::setName);
        Optional.ofNullable(productUpdateRequestDto.getDescription()).ifPresent(this::setDescription);
        Optional.ofNullable(productUpdateRequestDto.getUrl()).ifPresent(this::setUrl);
    }

}
