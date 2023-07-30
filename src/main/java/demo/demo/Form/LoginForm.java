package demo.demo.Form;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Data
public class LoginForm {

    @NotBlank(message = "아이디는 공백은 입력할 수 없습니다.")
    private String userId;

    @NotBlank(message = "비밀번호는 공백은 입력할 수 없습니다.")
    private String password;

    public LoginForm(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

}
