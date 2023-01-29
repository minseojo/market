package demo.demo;

import demo.demo.repository.MemoryProductRepository;
import demo.demo.repository.ProductRepository;
import demo.demo.service.ProductService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    @Bean
    public ProductService productService() {
        return new ProductService(productRepository());
    }
    @Bean
    public ProductRepository productRepository() {
        return new MemoryProductRepository();
    }

}
