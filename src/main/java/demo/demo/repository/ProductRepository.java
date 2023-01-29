package demo.demo.repository;

import demo.demo.domain.Product;

import java.util.List;
import java.util.Optional;


public interface ProductRepository {
    Product sava(Product product);
    Optional<Product> findById(Long id);
    Optional<Product> findByName(String name);
    List<Product> findAll();

    List<Product> findByFilter(String name);
}
