package demo.demo.domain;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class Product{
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    @Range(min = 0, max = 999999999)
    private Integer price;

    @NotNull
    // 체크로 고르게 끔. 즉 공백일 가능성은 제로, 하나를 무조건 선택해야함
    private String category;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
         this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
