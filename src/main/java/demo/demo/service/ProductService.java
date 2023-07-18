package demo.demo.service;

import demo.demo.domain.Product;
import demo.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Long create(Product product) {
        productRepository.sava(product);
        return product.getId();
    }

    public List<Product> findAllProduct() {
        return productRepository.findLimitTwenty();
    }


    public List<Product> findProduct(String name) {
        return productRepository.findByFilter(name);
    }

    public List<Product> findAllProductByName(String name) {
        return productRepository.findByFilter(name);
    }

    public Optional<Product> findOne(Long memberId) {
        return productRepository.findById(memberId);
    }

}
