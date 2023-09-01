package demo.demo.repository;

import demo.demo.domain.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.*;

@SpringBootTest
@Transactional

class JdbcProductRepositoryTest {
    @Autowired
    private final DataSource dataSource;
    ProductRepository productRepository;

    @Autowired
    JdbcProductRepositoryTest(DataSource dataSource) {
        this.dataSource = dataSource;
        this.productRepository = (ProductRepository) new JdbcProductRepository(dataSource);
    }

    @BeforeEach
    void beforeEach() {

    }


    @Test
    void sava() {
        Product product = Product.builder()
                .name("물")
                .price(1000)
                .category("음식")
                .build();


        Product product2 = productRepository.findById(product.getId()).get();
        Assertions.assertThat(product2).isNotNull();
        Assertions.assertThat(product2.getName()).isEqualTo("물");
        Assertions.assertThat(product2.getPrice()).isEqualTo(1000L);
        Assertions.assertThat(product2.getCategory()).isEqualTo("음식");

        //System.out.println(product.getId() + " " + product2.getId());
        /**
         *
         *         product2를 디비에서 가져와서 객체의 해시코드값은 다르고, 객체의 값은 같음.
         *         product3는 저장하는 동시에 저장된 객체를 가져옴
         *         그래서 product2의 값만 비교하는 동등성만 비교한다. (동등성x)
         *         결론: product, product3는 동일 (객체 해시코드가 같음)
         *              product, product는 동등 (객체 값만 같음)
         */

        Assertions.assertThat(product.getId()).isEqualTo(product2.getId());
        Assertions.assertThat(product.getName()).isEqualTo(product2.getName());
        Assertions.assertThat(product.getPrice()).isEqualTo(product2.getPrice());
        Assertions.assertThat(product.getCategory()).isEqualTo(product2.getCategory());
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

    @Test
    Connection getConnection() {

        return DataSourceUtils.getConnection(dataSource);
    }
}