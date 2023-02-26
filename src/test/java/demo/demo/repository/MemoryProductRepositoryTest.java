package demo.demo.repository;

import demo.demo.controller.SearchController;
import demo.demo.domain.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class MemoryProductRepositoryTest {

    MemoryProductRepository productRepository = new MemoryProductRepository();
    HashMap<Long, Product> store = new HashMap<>();

    @BeforeEach
    void beforeEach() {
        productRepository.clearStore();
    }
    @Test
    void sava() {
        Product product = new Product();
        product.setName("청소기");
        product.setPrice(1000L);
        product.setCategory("디지털가전");
        productRepository.sava(product);
        Product product1 = productRepository.findById(product.getId()).get();
        Assertions.assertThat(product).isEqualTo(product1);
    }

    @Test
    void findById() {
    }

    @Test
    void findByName() {
    }

    @Test
    void findAll() {
    }

}