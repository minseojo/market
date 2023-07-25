package demo.demo.repository;


import demo.demo.domain.Product;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;


public class MemoryProductRepository{
    private static Map<Long, Product> store = new HashMap<>();
    private static long sequence = 0L;

    public Product sava(Product product) {
        product.setId(++sequence);
        store.put(sequence, product);
        return product;
    }


    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(store.get(id)); // 널 처리
    }


    public Optional<Product> findByName(String name) {
        return store.values().stream()
                .filter(member -> member.getName().equals(name))
                .findAny();
    }


    public List<Product> findLimitTwenty() {
        return store.values().stream().collect(Collectors.toList());
    }

    public List<Product> findByFilter(String name) {
        return store.values().stream()
                .filter(member -> member.getName().equals(name))
                .collect(Collectors.toList());
    }


    public Optional<Product> findByCategory(String category) {
        return Optional.empty();
    }


    public List<Product> findAll() {
        return new ArrayList<>(store.values());
    }


    //test - after
    public void clearStore() {
        store.clear();
    }
}
