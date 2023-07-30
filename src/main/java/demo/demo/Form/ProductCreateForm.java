package demo.demo.Form;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ProductCreateForm {
    private Long id;
    @NotBlank(message = "공백은 입력할 수 없습니다.")
    private String name;

    @NotNull(message = "값을 입력해주세요.")
    @Range(min = 0, max = 999999999, message = "0에서 999999999 사이의 값을 입력해주세요.")
    private Integer price;

    @NotBlank(message = "카테고리를 골라주세요")
    // 체크로 고르게 끔. 즉 공백일 가능성은 제로, 하나를 무조건 선택해야함
    private String category;

    private List<MultipartFile> imageFiles;

    private Long ownerId;

}
