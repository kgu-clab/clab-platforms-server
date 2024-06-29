package page.clab.api.domain.product.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.product.application.ProductUpdateService;
import page.clab.api.domain.product.dao.ProductRepository;
import page.clab.api.domain.product.domain.Product;
import page.clab.api.domain.product.dto.request.ProductUpdateRequestDto;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class ProductUpdateServiceImpl implements ProductUpdateService {

    private final ValidationService validationService;
    private final ProductRepository productRepository;

    @Transactional
    @Override
    public Long update(Long productId, ProductUpdateRequestDto requestDto) {
        Product product = getProductByIdOrThrow(productId);
        product.update(requestDto);
        validationService.checkValid(product);
        return productRepository.save(product).getId();
    }

    private Product getProductByIdOrThrow(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("해당 서비스가 존재하지 않습니다."));
    }
}
