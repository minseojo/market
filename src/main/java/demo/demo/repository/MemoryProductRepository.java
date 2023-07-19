package demo.demo.repository;


import demo.demo.domain.Product;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;


public class MemoryProductRepository implements ProductRepository{
    private static Map<Long, Product> store = new HashMap<>();
    private static long sequence = 0L;
    @Override
    public Product sava(Product product) {
        product.setId(++sequence);
        store.put(sequence, product);
        return product;
    }

    @Override
    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(store.get(id)); // 널 처리
    }

    @Override
    public Optional<Product> findByName(String name) {
        return store.values().stream()
                .filter(member -> member.getName().equals(name))
                .findAny();
    }

    @Override
    public List<Product> findLimitTwenty() {
        return store.values().stream().collect(Collectors.toList());
    }
    @Override
    public List<Product> findByFilter(String name) {
        return store.values().stream()
                .filter(member -> member.getName().equals(name))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Product> findByCategory(String category) {
        return Optional.empty();
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(store.values());
    }


    //test - after
    public void clearStore() {
        store.clear();
    }
}
