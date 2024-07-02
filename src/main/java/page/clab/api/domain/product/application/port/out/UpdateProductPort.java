package page.clab.api.domain.product.application.port.out;

import page.clab.api.domain.product.domain.Product;

public interface UpdateProductPort {
    Product update(Product product);
}
