package demo.demo.domain;

import lombok.*;
import java.util.List;

@Getter
@Builder
public class Product{

    private Long id;

    private String name;

    private Integer price;
    private String category;

    private List<UploadFile> imageFiles;

    private String stringImageFiles;

    //yyyy-MM-dd HH:mm:ss (mysql DateTime)
    private String createDate;

    private Long ownerId;

    public boolean isOwner(Long userId) {
        return this.ownerId == userId;
    }

    public void setId(long id) {
        this.id = id;
    }
}
