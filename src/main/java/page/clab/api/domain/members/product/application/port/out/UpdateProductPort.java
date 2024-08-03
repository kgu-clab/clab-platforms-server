package page.clab.api.domain.members.product.application.port.out;

import page.clab.api.domain.members.product.domain.Product;

public interface UpdateProductPort {
    Product update(Product product);
}
