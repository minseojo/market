package demo.demo.repository;

import demo.demo.domain.Product;

import java.util.List;
import java.util.Optional;


public interface ProductRepository {

    // 상품 저장
    Product sava(Product product);

    // 상품 아이디로 찾기
    Optional<Product> findById(Long id);


    // 상품 이름으로 찾기
    Optional<Product> findByName(String name);

    // 상품 20개 찾기
    // 현재는 8개, 최신 순으로
    List<Product> findLimitTwenty();

    // 상품 전부 찾기
    List<Product> findAll();

    // 상품이름이 들어간 항목 전부 찾기
    List<Product> findByFilter(String name);

    // 선택한 카테로기 상품 보기
    Optional<Product> findByCategory(String category);
}
