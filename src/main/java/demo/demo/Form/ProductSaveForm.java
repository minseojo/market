package demo.demo.Form;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class ProductSaveForm {
    @NotBlank
    private String name;

    @NotNull
    @Range(min = 0, max = 999999999)
    private Integer price;

    @NotNull
    // 체크로 고르게 끔. 즉 공백일 가능성은 제로, 하나를 무조건 선택해야함
    private String category;

    private List<MultipartFile> imageFiles;

}
