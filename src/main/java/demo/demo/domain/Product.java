package demo.demo.domain;

public class Product {
    private Long Id;
    private String name;
    private Integer price;
    private String category;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
         this.Id = id;
    }

    public String getName() {
        return  name;
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
