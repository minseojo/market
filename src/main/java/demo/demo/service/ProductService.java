package demo.demo.service;

import demo.demo.Form.ProductCreateForm;
import demo.demo.domain.Product;
import demo.demo.repository.ProductRepository;
import demo.demo.utility.Time;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.AttributedString;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    private final ProductRepository productRepository;

    public List<Product> findLimitTwenty() {return productRepository.findLimitTwenty();}
    public List<Product> findAllProduct() {
        return productRepository.findAll();
    }

    public Optional<Product> findById(Long id) {return productRepository.findById(id);}
    public List<Product> findByFilter(String name) {
        return productRepository.findByFilter(name);
    }

    public List<String> getImageFileNames(Product product) {
        if (product.getStringImageFiles() == null) {
            return List.of("default_product.jpeg");
        } else {
            return List.of(product.getStringImageFiles().split(","));
        }
    }
    public String getProduct(Product product) {
        if (product == null) {
            return "errors/product-null";
        }

        return "products/product-view";
    }
}
