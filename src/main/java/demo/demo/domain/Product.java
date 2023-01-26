package demo.demo.domain;

public class Product {
    private Long Id;
    private String name;
    private int price;
    private String category;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
         this.Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
