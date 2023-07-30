package demo.demo.Form;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class SignupForm {
    private Long id;
    @NotBlank(message = "닉네임을 입력해 주세요.")
    private String nickname;  // 사용자가 이용할 닉네임
    @NotBlank(message = "아이디를 입력해 주세요.")
    private String userId; // Id
    @NotBlank(message = "비밀번호를 입력해 주세요.")
    private String password; // 비밀번호
    @NotBlank(message = "이름을 입력해 주세요.")
    private String name; // 이름

    @NotBlank(message = "이메일을 입력해 주세요.")
    @Email
    private String email; // 이메일

    @NotBlank(message = "휴대폰 번호를 입력해 주세요.")
    private String phoneNumber; // 휴대폰 번호

    @NotBlank(message = "주소 입력해 주세요.")
    private String address; //주소

    public SignupForm(){}

}
