package demo.demo.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class SignupForm {
    @NotBlank
    private String nickname;  // 사용자가 이용할 닉네임
    @NotBlank
    private String userId; // Id
    @NotBlank
    private String password; // 비밀번호

    @NotBlank
    private String name; // 이름
    @Email
    private String email; // 이메일

    @NotBlank
    private String phoneNumber; // 휴대폰 번호

    @NotBlank
    private String address; //주소

}
