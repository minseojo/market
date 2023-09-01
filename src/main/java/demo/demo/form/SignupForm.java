package demo.demo.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class SignupForm {
    @NotBlank(message = "{required.user.nickname}")
    private String nickname;  // 사용자가 이용할 닉네임
    @NotBlank(message = "{required.user.userId}")
    private String userId; // Id
    @NotBlank(message = "{required.user.password}")
    private String password; // 비밀번호
    @NotBlank(message = "{required.user.name}")
    private String name; // 이름

    @NotBlank(message = "{required.user.email}")
    @Email
    private String email; // 이메일

    @NotBlank(message = "{required.user.phoneNumber}")
    private String phoneNumber; // 휴대폰 번호

    @NotBlank(message = "{required.user.address}")
    private String address; //주소
}
