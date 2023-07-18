package demo.demo;

import demo.demo.repository.JdbcProductRepository;
import demo.demo.repository.MemoryProductRepository;
import demo.demo.repository.ProductRepository;
import demo.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class SpringConfig {
    private final DataSource dataSource;

    @Autowired
    public SpringConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    //@Bean
    public ProductService productService() {
        return new ProductService(productRepository());
    }

    //@Bean
    public ProductRepository productRepository() {
        //return new MemoryProductRepository();
        return new JdbcProductRepository(dataSource);
    }

}
