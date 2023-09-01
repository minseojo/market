package demo.demo.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginForm {

    @NotBlank(message = "{required.userId.id}")
    private String userId;

    @NotBlank(message = "{required.user.password}")
    private String password;

    public LoginForm(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

}
