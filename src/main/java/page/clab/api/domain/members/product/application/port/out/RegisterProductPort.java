package page.clab.api.domain.members.product.application.port.out;

import page.clab.api.domain.members.product.domain.Product;

public interface RegisterProductPort {
    Product save(Product product);
}
