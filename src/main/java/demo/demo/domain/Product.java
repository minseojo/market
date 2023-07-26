package demo.demo.domain;

import lombok.*;
import org.hibernate.validator.constraints.Range;
import org.springframework.context.annotation.Primary;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Builder
public class Product{

    @Builder.Default
    private Long id = -1L;

    @NotBlank
    private String name;

    @NotNull
    @Range(min = 0, max = 999999999)
    private Integer price;
    @NotNull
    // 체크로 고르게 끔. 즉 공백일 가능성은 제로, 하나를 무조건 선택해야함
    private String category;

    @Nullable
    private List<UploadFile> imageFiles;

    //images (db)
    @Nullable
    private String stringImageFiles;

    //yyyy-MM-dd HH:mm:ss (mysql DateTime)
    @NotNull
    private String createDate;


}
