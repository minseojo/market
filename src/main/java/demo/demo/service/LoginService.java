package demo.demo.service;

import demo.demo.form.LoginForm;
import demo.demo.form.SignupForm;
import demo.demo.domain.User;
import demo.demo.utility.Time;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserService userService;
    private final Time time;
    public boolean login(User user, LoginForm form) {
        return user.getPassword().equals(form.getPassword());
    }

    public Long signup(SignupForm form) {
        User user = User.builder()
                .userId(form.getUserId())
                .password(form.getPassword())
                .nickname(form.getNickname())
                .name(form.getName())
                .email(form.getEmail())
                .address(form.getAddress())
                .phoneNumber(form.getPhoneNumber())
                .registrationDate(time.getCurrentTime())
                .build();

        return userService.create(user);
    }

}
