package demo.demo.controller;

import demo.demo.Form.LoginForm;
import demo.demo.Form.SignupForm;
import demo.demo.domain.User;
import demo.demo.service.LoginService;
import demo.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {
    private final UserService userService;
    private final LoginService loginService;
    @GetMapping("/login")
    public String loginHome() {
        return "login/login-view";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginForm form, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            log.info("{}", result);
        }

        User user = userService.findByUserId(form.getUserId()).orElse(null);

        if (user == null) {
            return "redirect:/login";
        }

        if (loginService.login(user, form)) {
            redirectAttributes.addAttribute("id", user.getId());
            return "redirect:/users/{id}";
        }

        return "redirect:/login";
    }

    @GetMapping("/signup")
    public String signupHome() {
        return "login/signup-view";
    }

    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute SignupForm form, BindingResult result, RedirectAttributes redirectAttributes)  {
        if (result.hasErrors()) {
            log.info("{}", result);
        }

        Long id = loginService.signup(form);

        redirectAttributes.addAttribute("id", id);
        return "redirect:/users/{id}";
    }

}
