package demo.demo.form;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class ProductCreateForm {
    private Long id;
    @NotBlank(message = "{required.product.productName}")
    private String name;

    @NotNull(message = "{required.product.price}")
    @Range(min = 0, max = 999999999, message = "${range.product.price}")
    private Integer price;

    @NotBlank(message = "{required.product.category}}")
    // 체크로 고르게 끔. 즉 공백일 가능성은 제로, 하나를 무조건 선택해야함
    private String category;

    private List<MultipartFile> imageFiles;

    private Long ownerId;

}
