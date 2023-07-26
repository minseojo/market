package demo.demo.controller;

import demo.demo.Form.LoginForm;
import demo.demo.Form.SignupForm;
import demo.demo.domain.User;
import demo.demo.service.UserService;
import demo.demo.utility.Time;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class LoginController {
    private final UserService userService;
    private final Time time;
    @GetMapping("/login")
    public String loginHome() {
        return "login/login-view";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginForm form, BindingResult result, RedirectAttributes redirectAttributes) throws IOException {
        String userId = form.getUserId();
        Optional<User> user = userService.findByUserId(userId);
        if(user.isPresent()) {
            if(form.getPassword().equals(user.get().getPassword())) {
                redirectAttributes.addAttribute("id", user.get().getId());
                return "redirect:/users/{id}";
            }
        }
        return "redirect:/login";
    }

    @GetMapping("/signup")
    public String signupHome() {
        return "login/signup-view";
    }

    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute SignupForm form, BindingResult result, RedirectAttributes redirectAttributes) throws IOException {
        User user = User.builder()
                .userId(form.getUserId())
                .password(form.getPassword())
                .nickname(form.getNickname())
                .name(form.getName())
                .email(form.getEmail())
                .address(form.getAddress())
                .phoneNumber(form.getPhoneNumber())
                .registrationDate(time.getTime())
                .build();
        userService.create(user);

        redirectAttributes.addAttribute("id", user.getId());
        return "redirect:/users/{id}";
    }

}
