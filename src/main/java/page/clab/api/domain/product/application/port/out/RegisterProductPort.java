package page.clab.api.domain.product.application.port.out;

import page.clab.api.domain.product.domain.Product;

public interface RegisterProductPort {
    Product save(Product product);
}
