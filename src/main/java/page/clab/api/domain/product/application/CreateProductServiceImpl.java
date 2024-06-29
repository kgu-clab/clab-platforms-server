package page.clab.api.domain.product.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.product.dao.ProductRepository;
import page.clab.api.domain.product.domain.Product;
import page.clab.api.domain.product.dto.request.ProductRequestDto;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class CreateProductServiceImpl implements CreateProductService {

    private final ValidationService validationService;
    private final ProductRepository productRepository;

    @Transactional
    @Override
    public Long execute(ProductRequestDto requestDto) {
        Product product = ProductRequestDto.toEntity(requestDto);
        validationService.checkValid(product);
        return productRepository.save(product).getId();
    }
}
