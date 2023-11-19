package page.clab.api.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.exception.NotFoundException;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.repository.ProductRepository;
import page.clab.api.type.dto.ProductRequestDto;
import page.clab.api.type.dto.ProductResponseDto;
import page.clab.api.type.entity.Member;
import page.clab.api.type.entity.Product;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final MemberService memberService;

    private final ProductRepository productRepository;


    public void createProduct(ProductRequestDto productRequestDto) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        if (!memberService.isMemberAdminRole(member)) {
            throw new PermissionDeniedException("서비스를 등록 권한이 없습니다.");
        }
        Product product = Product.of(productRequestDto);
        productRepository.save(product);
    }

    public List<ProductResponseDto> getProducts(Pageable pageable) {
        Page<Product> products = productRepository.findAllByOrderByCreatedAtDesc(pageable);
        return products.map(ProductResponseDto::of).getContent();
    }

    public List<ProductResponseDto> searchProduct(String productName, Pageable pageable) {
        Page<Product> products;
        if (productName != null) {
            products = getProductByNameContaining(productName, pageable);
        } else {
            throw new IllegalArgumentException("검색어를 입력해주세요.");
        }
        if (products.isEmpty()) {
            throw new NotFoundException("검색 결과가 없습니다.");
        }
        return products.map(ProductResponseDto::of).getContent();
    }

    public void updateProduct(Long productId, ProductRequestDto productRequestDto) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        if (!memberService.isMemberAdminRole(member)) {
            throw new PermissionDeniedException("서비스를 수정할 권한이 없습니다.");
        }
        Product product = getProductByIdOrThrow(productId);
        Product updatedProduct = Product.of(productRequestDto);
        updatedProduct.setId(product.getId());
        updatedProduct.setCreatedAt(product.getCreatedAt());
        productRepository.save(updatedProduct);
    }

    public void deleteProduct(Long productId) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        if (!memberService.isMemberAdminRole(member)) {
            throw new PermissionDeniedException("서비스를 삭제할 권한이 없습니다.");
        }
        Product product = getProductByIdOrThrow(productId);
        productRepository.delete(product);
    }

    private Product getProductByIdOrThrow(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("해당 서비스가 존재하지 않습니다."));
    }

    private Page<Product> getProductByNameContaining(String productName, Pageable pageable) {
        return productRepository.findAllByNameContainingOrderByCreatedAtDesc(productName, pageable);
    }

}
