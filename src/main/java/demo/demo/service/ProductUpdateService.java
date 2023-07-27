package demo.demo.service;

import demo.demo.Form.ProductUpdateForm;
import demo.demo.domain.Product;
import demo.demo.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional
public class ProductUpdateService {

    private final ProductRepository productRepository;

    public Product update(ProductUpdateForm form) {
        Product product = Product.builder()
            .id(form.getId())
            .name(form.getName())
            .price(form.getPrice())
            .category(form.getCategory())
            .build();

        return productRepository.update(product);
    }

    public ProductUpdateForm createProduct(Product product) {
        return ProductUpdateForm.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .category(product.getCategory())
                .createDate(product.getCreateDate())
                .build();
    }

    public List<String> getImageFileNames(Product product) {
        List<String> imageFileNames;
        if (product.getStringImageFiles() == null) {
            imageFileNames = List.of("default_product.jpeg");
        } else {
            imageFileNames = List.of(product.getStringImageFiles().split(","));
        }
        return imageFileNames;
    }
}
