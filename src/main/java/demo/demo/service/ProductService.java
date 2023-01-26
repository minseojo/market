package demo.demo.service;

import demo.demo.domain.Product;
import demo.demo.repository.MemoryProductRepository;
import demo.demo.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

public class ProductService {

    private final ProductRepository productRepository;
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Long create(Product product) {
        productRepository.sava(product);
        return product.getId();
    }

    public List<Product> findProduct() {
        return productRepository.findAll();
    }

    public Optional<Product> findOne(Long memberId) {
        return productRepository.findById(memberId);
    }
}
