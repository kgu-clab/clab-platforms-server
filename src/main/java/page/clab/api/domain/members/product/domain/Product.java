package page.clab.api.domain.members.product.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.members.product.application.dto.request.ProductUpdateRequestDto;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Product {

    private Long id;
    private String name;
    private String description;
    private String url;
    private boolean isDeleted;
    private LocalDateTime createdAt;

    public void update(ProductUpdateRequestDto productUpdateRequestDto) {
        Optional.ofNullable(productUpdateRequestDto.getName()).ifPresent(this::setName);
        Optional.ofNullable(productUpdateRequestDto.getDescription()).ifPresent(this::setDescription);
        Optional.ofNullable(productUpdateRequestDto.getUrl()).ifPresent(this::setUrl);
    }

    public void delete() {
        this.isDeleted = true;
    }
}
