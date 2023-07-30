package demo.demo.Form;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
public class ProductUpdateForm {
    private Long id;

    @NotBlank(message = "상품명을 입력해 주세요.")
    private String name;

    @NotNull(message = "가격을 입력해 주세요.")
    @Range(min = 0, max = 999999999, message = "0에서 999999999 사이의 값을 입력해주세요.")
    private Integer price;

    @NotBlank(message = "카테고리를 골라 주세요.")
    // 체크로 고르게 끔. 즉 공백일 가능성은 제로, 하나를 무조건 선택해야함
    private String category;

    private List<MultipartFile> imageFiles;

}
